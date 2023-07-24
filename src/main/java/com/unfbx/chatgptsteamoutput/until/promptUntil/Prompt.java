package com.unfbx.chatgptsteamoutput.until.promptUntil;

public interface Prompt {
    String prompt = null;

    public default String getPrompt(){
        return "error,No prompt!";
    };

}
