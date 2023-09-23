package com.unfbx.chatgptsteamoutput.until.OpenAIRequstUntil;


import java.io.IOException;
import java.util.List;

import cn.hutool.core.convert.ConvertException;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OpenAIRequest {
    private String apiKey = "Bearer "; //这里应该通过依赖注入的方式注入
    private String apiHost = "https://api.openai.com/v1/chat/completions";

    private String model = "gpt-3.5-turbo-0301"; //GPT版本信息，就用3.5吧
    private String prompt;
    private String userMessage;
    private String result;

    public OpenAIRequest(String prompt, String userMessage) throws IOException {
        this.prompt = prompt;
        this.userMessage = userMessage;
        //构造之后直接完成请求发送和处理
    }
    public OpenAIRequest(String userMessage) {
        this.userMessage = userMessage;
    }

    private String sendRequest() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("model", model);
        List<Map<String, String>> dataList = new ArrayList<>();
        dataList.add(new HashMap<String, String>() {{
            put("role", "user");
            put("content", prompt + userMessage);
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
            return "网络返回异常，message详细信息:" + message;
        } catch (ConvertException e) {
            return "格式转换异常";
        }
        return message.getStr("content");
    }

    public String getResult() {
        return sendRequest();
    }
}
