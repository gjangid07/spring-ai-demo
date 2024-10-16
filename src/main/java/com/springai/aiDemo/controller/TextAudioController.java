package com.springai.aiDemo.controller;

import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TextAudioController {

  private final OpenAiAudioSpeechModel openAiAudioSpeechModel;

  public TextAudioController(OpenAiAudioSpeechModel openAiAudioSpeechModel) {
    this.openAiAudioSpeechModel = openAiAudioSpeechModel;
  }

  @GetMapping("/text-audio")
  public ResponseEntity<Resource> getAudioFromText(@RequestParam(value = "text") String text){
    OpenAiAudioSpeechOptions options = OpenAiAudioSpeechOptions.builder()
        .withSpeed(1.0f)
        .withVoice(OpenAiAudioApi.SpeechRequest.Voice.FABLE)
        .withModel("tts-1") // ID of the model to use. Only tts-1 is currently available.
        .build();

    SpeechPrompt speechPrompt = new SpeechPrompt(text, options);
    byte[] bytes = openAiAudioSpeechModel.call(speechPrompt).getResult().getOutput();

    ByteArrayResource byteArrayResource = new ByteArrayResource(bytes);

    return ResponseEntity
        .ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .contentLength(byteArrayResource.contentLength())
        .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition
            .attachment()
            .filename("textToAudio.mp3")
            .build().toString())
        .body(byteArrayResource);
  }
}
