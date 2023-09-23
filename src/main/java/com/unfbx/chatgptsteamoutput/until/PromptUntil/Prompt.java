package com.unfbx.chatgptsteamoutput.until.PromptUntil;

import org.springframework.stereotype.Component;

@Component
public interface Prompt {
    String prompt = null;

    public default String getPrompt(){
        return "error,No prompt!";
    };

}
