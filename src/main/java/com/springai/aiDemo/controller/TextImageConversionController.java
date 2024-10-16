package com.springai.aiDemo.controller;

import org.springframework.ai.image.ImageMessage;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.image.ImageOptionsBuilder;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TextImageConversionController {

  private final ImageModel imageModel;

  public TextImageConversionController(ImageModel imageModel) {
    this.imageModel = imageModel;
  }

  @GetMapping("/text-image")
  public String getImage(@RequestParam(value = "text") String text){
    return imageModel.call(new ImagePrompt(new ImageMessage(text), ImageOptionsBuilder.builder()
        .withHeight(1024)
        .withWidth(1024)
        .withN(1).build())).getResult().getOutput().getUrl();
  }
}
