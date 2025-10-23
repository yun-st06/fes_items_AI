package com.example.app.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.responses.CreateResponseRequest;
import com.openai.models.responses.CreateResponseResponse;


@Service
public class OpenAiClient {

    private final OpenAIClient client;
    private final String model;

    public OpenAiClient(
            @Value("${openai.api.key}") String apiKey,
            @Value("${openai.model}") String model
    ) {
        this.model = model;
        this.client = new OpenAIOkHttpClient(apiKey);
    }

    public String ask(String prompt) {
    	CreateResponseRequest request = CreateResponseRequest.builder()
    		    .model(model)
    		    .input(prompt)
    		    .build();

    		CreateResponseResponse response = client.responses().create(request);
    		return response.outputText().orElse("(no text)");

    }
}
