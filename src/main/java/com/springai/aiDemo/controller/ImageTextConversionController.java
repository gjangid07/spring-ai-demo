package com.springai.aiDemo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.Media;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ImageTextConversionController {

  private final ChatClient chatClient;

  public ImageTextConversionController(ChatClient.Builder chatClientBuilder) {
    this.chatClient = chatClientBuilder.build();
  }

  @GetMapping("/image-describe")
  public String getImageDescription() throws IOException {
    byte[] imageBytes = new ClassPathResource("images/image1.jpg").getContentAsByteArray();
    UserMessage userMessage = new UserMessage("Could you please describe the image provided to you", List.of(new Media(MimeTypeUtils.IMAGE_JPEG, imageBytes)));
    return chatClient.prompt(new Prompt(userMessage)).call().content();
  }

  @GetMapping("/code-describe")
  public String getCodeDescription() throws IOException {
    byte[] imageBytes = new ClassPathResource("images/JavaCodeImage.png").getContentAsByteArray();
    UserMessage userMessage = new UserMessage("Could you please describe the image provided to you", List.of(new Media(MimeTypeUtils.IMAGE_PNG, imageBytes)));
    return chatClient.prompt(new Prompt(userMessage)).call().content();
  }
}
