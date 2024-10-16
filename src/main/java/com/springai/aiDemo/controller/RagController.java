package com.springai.aiDemo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class RagController {

  private final ChatClient chatClient;
  private final VectorStore vectorStore;

  @Value("classpath:/prompts/rag-prompt-template.st")
  private Resource ragPromptTemplate;

  public RagController(ChatClient.Builder builder, VectorStore vectorStore) {
    this.chatClient = builder.build();
    this.vectorStore = vectorStore;
  }

  @GetMapping("/faq-answer")
  public String getAnswer(@RequestParam(value = "question") String question){
    List<Document> similarDocuments = vectorStore.similaritySearch(SearchRequest.query(question).withTopK(2));
    List<String> documentContents = similarDocuments.stream().map(Document::getContent).collect(Collectors.toList());
    PromptTemplate template = new PromptTemplate(ragPromptTemplate);
    Prompt prompt = template.create(Map.of("input", question, "documents", String.join("\n", documentContents)));
    return chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getContent();
  }

}
