package com.unfbx.chatgptsteamoutput.until.RequestTypeUntil;

import com.unfbx.chatgptsteamoutput.until.OpenAIRequstUntil.OpenAIRequest;
import com.unfbx.chatgptsteamoutput.until.PromptUntil.Prompt4SQL;
import com.unfbx.chatgptsteamoutput.until.PromptUntil.SystemPrompt.Prompt4DataAsync;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据库交互
 */

@Component
public class SQLChat implements RequestType {
    @Autowired
    private Prompt4SQL prompt4SQL;

    @Override
    public RequestTypeEnum getRequestType() {
        return RequestTypeEnum.SQL;
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public String getChatResult(String question) throws ExecutionException, InterruptedException {
        //任务分为三步，1：生成SQL 2.数据查询 3.文本回调
        //1.生成SQL语句
        CompletableFuture<String> genSQLData = CompletableFuture.supplyAsync(() -> {
            try {
                OpenAIRequest openAIRequest = new OpenAIRequest(prompt4SQL.getPrompt(), question);
                return openAIRequest.getResult();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        //2.SQL查询，获取真实数据
        CompletableFuture<String> asyncResult = genSQLData.thenApply(result -> {
            Pattern pattern = Pattern.compile("SQLQuery: \"(.*?)\"", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(result);
            String SQL = null;
            while (matcher.find()) {
                SQL = matcher.group(1);
            }
            if (SQL == null) throw new IllegalArgumentException("SQL is null!");
            List<Map<String, Object>> list = jdbcTemplate.queryForList(SQL);
            StringBuilder queryResultSb = new StringBuilder();
            for (Map<String, Object> map : list) {
                Set<Map.Entry<String, Object>> entries = map.entrySet();
                for (Map.Entry<String, Object> entry : entries) {
                    String key = entry.getKey();
                    String value = (String) entry.getValue();
                    queryResultSb.append(key).append(value).append("\n");
                }
            }
            return queryResultSb.toString();
        });
        //组合两次调用的结果，并进行文本回调
        genSQLData.thenCombine(asyncResult, (SQLPrompt, SQLResult) -> SQLPrompt + Prompt4DataAsync.getPrompt() + SQLResult);
        return new OpenAIRequest(genSQLData.get()).getResult();
    }
}
