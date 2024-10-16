package com.springai.aiDemo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CityDetailsController {

  private final ChatClient chatClient;

  public CityDetailsController(ChatClient.Builder builder) {
    this.chatClient = builder.build();
  }

  @GetMapping("/city-details")
  public String getCityUpdate(@RequestParam(value = "message") String message){
    UserMessage userMessage = new UserMessage(message);
    AssistantMessage assistantMessage = new AssistantMessage("Assume you are an AI assistant and provide with the detail information of the city");
    ChatOptions chatOptions = OpenAiChatOptions.builder()
        .withTemperature(1.0f)
        .withFunction("weatherService").build();

    Prompt prompt = new Prompt(List.of(userMessage, assistantMessage), chatOptions);
    return chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getContent();
  }
}
