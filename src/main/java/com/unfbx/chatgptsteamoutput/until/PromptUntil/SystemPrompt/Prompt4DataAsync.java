package com.unfbx.chatgptsteamoutput.until.PromptUntil.SystemPrompt;

public class Prompt4DataAsync {
    private static String prompt = "下面这些数据是通过SQL查询语句在真实数据库中查询到的数据结果，当用户问你数据相关的问题时，你可以根据下面的数据进行回答，数据：";

    public static String getPrompt() {
        return prompt;
    }
}
