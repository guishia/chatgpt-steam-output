package com.unfbx.chatgptsteamoutput.until.RequestTypeUntil;

import org.springframework.web.bind.annotation.RequestBody;

import java.util.concurrent.ExecutionException;

public interface RequestType {
    /*
    获取ChatGPT的调用方式
     */
    RequestTypeEnum getRequestType();

    /**
     * @param question 用户输入的自然语言
     * @return 响应，GPT返回的文本
     **/
    String getChatResult(String question) throws ExecutionException, InterruptedException;
}
