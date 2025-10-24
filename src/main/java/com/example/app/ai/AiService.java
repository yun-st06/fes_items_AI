package com.example.app.ai;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

@Service
public class AiService {
    private final OpenAiService service;

    public AiService(@Value("${openai.api.key}") String apiKey) {
        this.service = new OpenAiService(apiKey, Duration.ofSeconds(60));
    }

    public String askOnce(String userPrompt) {
        var req = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo") // まずはこれでOK。後で gpt-4o-mini に差し替え可
                .messages(List.of(new ChatMessage("user", userPrompt)))
                .maxTokens(150)
                .build();

        var res = service.createChatCompletion(req);
        return res.getChoices().get(0).getMessage().getContent();
    }
}

