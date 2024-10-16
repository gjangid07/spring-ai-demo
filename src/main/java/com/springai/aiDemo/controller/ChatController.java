package com.springai.aiDemo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatController {

  private final ChatClient chatClient;

  public ChatController(ChatClient.Builder chatClientBuilder) {
    this.chatClient = chatClientBuilder.build();
  }

  @GetMapping("/chat")
  public String getChatResponse(@RequestParam(value = "", defaultValue = "tell me a single lame joke on Computers") String message){
    Prompt prompt = new Prompt(message);
    return chatClient.prompt(prompt).call().content();
//    return chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getContent();
  }
}
