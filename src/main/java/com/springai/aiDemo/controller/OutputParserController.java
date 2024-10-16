package com.springai.aiDemo.controller;

import com.springai.aiDemo.dto.Author;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.ai.parser.BeanOutputParser;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class OutputParserController {

  private final ChatClient chatClient;

  public OutputParserController(ChatClient.Builder builder) {
    this.chatClient = builder.build();
  }

  // List
  @GetMapping("/artist-songs")
  public List<String> getArtists(@RequestParam(value = "artistName") String artistName){
    String message = "Give me the list of top 3 songs of artist={artistName}. Provide answer in format={format}";
    ListOutputConverter listOutputConverter = new ListOutputConverter(new DefaultConversionService());

    PromptTemplate promptTemplate = new PromptTemplate(message, Map.of("artistName", artistName, "format", listOutputConverter.getFormat()));
    Prompt prompt = promptTemplate.create();
    return listOutputConverter.convert(chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getContent());
  }

  // Map
  @GetMapping("/artist-albums")
  public Map<String, Object> getAlbums(@RequestParam(value = "artistName") String artistName){
    String message = "Give me the list of top 3 albums details of artist={artistName}. Provide answer in format={format}";
    MapOutputConverter mapOutputConverter = new MapOutputConverter();

    PromptTemplate promptTemplate = new PromptTemplate(message, Map.of("artistName", artistName, "format", mapOutputConverter.getFormat()));
    Prompt prompt = promptTemplate.create();
    return mapOutputConverter.convert(chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getContent());
  }

  // bean output convertor
  @GetMapping("/author")
  public Author getAuthor(@RequestParam(value = "authorName") String authorName){
    String message = "Give me the list of top 3 books written by author={authorName}. Provide answer in format={format}";

    BeanOutputConverter<Author> beanOutputConverter = new BeanOutputConverter<>(Author.class);

    PromptTemplate promptTemplate = new PromptTemplate(message, Map.of("authorName", authorName, "format", beanOutputConverter.getFormat()));
    Prompt prompt = promptTemplate.create();
    return beanOutputConverter.convert(chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getContent());
  }

}
