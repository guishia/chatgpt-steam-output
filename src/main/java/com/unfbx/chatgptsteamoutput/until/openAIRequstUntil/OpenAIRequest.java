package com.unfbx.chatgptsteamoutput.until.openAIRequstUntil;

import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;

import cn.hutool.core.convert.ConvertException;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.unfbx.chatgptsteamoutput.until.promptUntil.*;
import org.springframework.beans.factory.annotation.Value;

public class OpenAIRequest {
    private String apiKey = "";
    private String apiHost = "https://api.openai.com/v1/chat/completions";
    private String SQLPrompt = new Prompt4SQL().getPrompt();
    private String translationPrompt = new Prompt4Translation().getPrompt();
    private String model = "gpt-3.5-turbo-0301"; //GPT版本信息，就用3.5吧
    private String realPrompt;
    private String userMessage;
    private String result;

    public OpenAIRequest(String promptType, String userMessage) throws IOException {
        if (promptType.equals("SQL")) this.realPrompt = SQLPrompt;
        this.userMessage = userMessage;
        //构造之后直接完成请求发送和处理
        this.result = sendRequest();
    }

    private String sendRequest() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("model", model);
        List<Map<String, String>> dataList = new ArrayList<>();
        dataList.add(new HashMap<String, String>() {{
            put("role", "user");
            put("content", realPrompt + userMessage);
        }});
        paramMap.put("messages", dataList);
        JSONObject message = null;
        try {
            String body = HttpRequest.post(apiHost)
                    .header("Authorization", apiKey)
                    .header("Content-Type", "application/json")
                    .body(JSONUtil.toJsonStr(paramMap))
                    .execute()
                    .body();
            JSONObject jsonObject = JSONUtil.parseObj(body);
            JSONArray choices = jsonObject.getJSONArray("choices");
            JSONObject result = choices.get(0, JSONObject.class, Boolean.TRUE);
            message = result.getJSONObject("message");
        } catch (HttpException e) {
            return "出现了异常";
        } catch (ConvertException e) {
            return "出现了异常";
        }
        return message.getStr("content");
    }

    public String getResult() {
        return this.result;
    }
}
