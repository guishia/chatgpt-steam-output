package com.unfbx.chatgptsteamoutput.until.promptUntil;

import org.springframework.stereotype.Component;

/*
    仿LangChain的简陋版SQL prompt
 */
@Component
public class Prompt4SQL implements Prompt {
    int top_k=4;
    //可以用的表
    String tableInfo="user";
    String  prompt= "You are a MySQL expert. Given an input question, first create a syntactically correct MySQL query to run, then look at the results of the query and return the answer to the input question.\n" +
            "Unless the user specifies in the question a specific number of examples to obtain, query for at most"+top_k+"results using the LIMIT clause as per MySQL. You can order the results to return the most informative data in the database.\n" +
            "Never query for all columns from a table. You must query only the columns that are needed to answer the question. Wrap each column name in backticks (`) to denote them as delimited identifiers.\n" +
            "Pay attention to use only the column names you can see in the tables below. Be careful to not query for columns that do not exist. Also, pay attention to which column is in which table.\n\n" +
            "Use the following format:\n\n" +
            "Question: \"Question here\"\n" +
            "SQLQuery: \"SQL Query to run\"\n" +
            "SQLResult: \"Result of the SQLQuery\"\n" +
            "Answer: \"Final answer here\"\n\n" +
            "Only use the following tables:\n" +
             tableInfo+"\n\n" +
            "Question: ";

    public Prompt4SQL(int top_k, String tableInfo) {
        this.top_k = top_k;
        this.tableInfo = tableInfo;
    }

    public Prompt4SQL() {

    }

    @Override
    public String getPrompt(){
        return prompt;
    }
}
