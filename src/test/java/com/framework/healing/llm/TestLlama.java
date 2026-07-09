package com.framework.healing.llm;

public class TestLlama {

    public static void main(String[] args) {

        OllamaClient client =
                new OllamaClient();

        String response =
                client.askLlama(
                        """
                        You are a Selenium Self-Healing AI.
        
                        Failed Locator ID:
                        user-name-WRONG
        
                        The locator was originally intended to identify a username input field.
        
                        Available Candidate Elements:
        
                        Candidate 1
                        ID=user-name
                        TAG=input
                        TYPE=text
        
                        Candidate 2
                        ID=password
                        TAG=input
                        TYPE=password
        
                        Candidate 3
                        ID=login-button
                        TAG=input
                        TYPE=submit
        
                        Compare the failed locator name with the candidate IDs.
        
                        Give highest importance to:
                        1. Similarity of ID names
                        2. Similarity of purpose
                        3. Similarity of element type
        
                        Return ONLY the best candidate ID.
                        """
                );

        System.out.println(response);
    }
}