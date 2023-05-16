package com.unfbx.chatgptsteamoutput.until;

public interface prompt {
    String prompt = null;

    public default String getPrompt(){
        return "error,No prompt!";
    };

}
