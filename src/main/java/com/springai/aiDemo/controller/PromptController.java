package com.springai.aiDemo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.AssistantPromptTemplate;
import org.springframework.ai.chat.prompt.FunctionPromptTemplate;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PromptController {

  private final ChatClient chatClient;

  @Value("classpath:/prompts/movie.st")
  private Resource resource;

  public PromptController(ChatClient.Builder builder) {
    this.chatClient = builder.build();
  }

  //User Role
  @GetMapping("/books")
  public String getBooks(@RequestParam(defaultValue = "fiction") String category, @RequestParam(defaultValue = "2023") String year){
    String message = "Give me the top book in the {category} published in the {year}.";
    PromptTemplate promptTemplate = new PromptTemplate(message);
    Prompt prompt = promptTemplate.create(Map.of("category", category, "year", year));
    return chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getContent();
  }


  // User Role With Resources
  @GetMapping("/movies")
  public String getMovies(@RequestParam(defaultValue = "comedy") String genre, @RequestParam(defaultValue = "2015") String year){
    PromptTemplate promptTemplate = new PromptTemplate(resource);
    Prompt prompt = promptTemplate.create(Map.of("genre", genre, "year", year));
    return chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getContent();
  }

  // System Role
  @GetMapping("/jokes")
  public String getJokes(@RequestParam(defaultValue = "animal joke") String userMsg){
    var systemMessage = new SystemMessage("You can only tell jokes about human and if someone asks any other type of jokes, give them math problem.");
    var userMessage = new UserMessage(userMsg);
    return chatClient.prompt(new Prompt(List.of(systemMessage, userMessage))).call().chatResponse().getResult().getOutput().getContent();
  }

  // Assistant Role
  @GetMapping("/ai-assistant")
  public String askAIAssistant(@RequestParam(value = "name") String name, @RequestParam(value = "question") String question){
    AssistantPromptTemplate assistantPromptTemplate = new AssistantPromptTemplate("User {name} asks: '{question}'. How would you respond as an AI assistant?");
    Prompt prompt = assistantPromptTemplate.create(Map.of("name", name, "question", question));
    return chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getContent();
  }

  //Function Role

  @GetMapping("/function")
  public String getFunctionResponse(@RequestParam(value = "function") String function, @RequestParam(value = "userInput") String userInput){
    FunctionPromptTemplate functionPromptTemplate = new FunctionPromptTemplate("Execute function {function} with input: {userInput}");
    Prompt prompt = functionPromptTemplate.create(Map.of("function", function, "userInput", userInput));
    return chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getContent();
  }
}
