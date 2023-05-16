package com.unfbx.chatgptsteamoutput.until;

public class prompt4Translation implements prompt {
    String sourceLanguage="";
    String targetLanguage="";

    String prompt="systemMessage：You are a language translation expert, and your job is to translate sourceLanguage into targetLanguage using the following template:\n"+
            "sourceLanguage:"+sourceLanguage+"\n"+
            "targetLanguage："+targetLanguage+"\n"+
            "user Input:";

    public prompt4Translation(String sourceLanguage, String targetLanguage){
          this.sourceLanguage+=sourceLanguage;
          this.targetLanguage+=targetLanguage;
    }

    @Override
    public String getPrompt() {
        return prompt;
    }
}