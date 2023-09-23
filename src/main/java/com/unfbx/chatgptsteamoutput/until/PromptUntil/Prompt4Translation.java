package com.unfbx.chatgptsteamoutput.until.PromptUntil;

import org.springframework.stereotype.Component;

@Component
public class Prompt4Translation implements Prompt {
    String sourceLanguage="";
    String targetLanguage="";

    public Prompt4Translation(String sourceLanguage, String targetLanguage){
          this.sourceLanguage+=sourceLanguage;
          this.targetLanguage+=targetLanguage;
    }

    public Prompt4Translation() {

    }

    @Override
    public String getPrompt() {
        return "systemMessage：You are a language translation expert, and your job is to translate sourceLanguage into targetLanguage using the following template:\n"+
                "sourceLanguage:"+sourceLanguage+"\n"+
                "targetLanguage："+targetLanguage+"\n"+
                "只需要输出翻译后的文字，其他任何都不用输出"+"\n"+
                "user Input:";
    }
}