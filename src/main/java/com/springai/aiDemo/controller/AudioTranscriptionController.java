package com.springai.aiDemo.controller;

import org.springframework.ai.audio.transcription.AudioTranscription;
import org.springframework.ai.audio.transcription.AudioTranscriptionOptions;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AudioTranscriptionController {

  private final OpenAiAudioTranscriptionModel aiAudioTranscriptionModel;

  @Value("classpath:/audios/harvard.wav")
  private Resource audioResource;


  public AudioTranscriptionController(OpenAiAudioTranscriptionModel aiAudioTranscriptionModel) {
    this.aiAudioTranscriptionModel = aiAudioTranscriptionModel;
  }

  @GetMapping("/audio-transcription")
  public String getAudioTranscription() {
    AudioTranscriptionOptions audioTranscriptionOptions = OpenAiAudioTranscriptionOptions.builder()
        .withLanguage("en")
        .withResponseFormat(OpenAiAudioApi.TranscriptResponseFormat.TEXT)
        .withTemperature(0f).build();

    AudioTranscriptionPrompt prompt = new AudioTranscriptionPrompt(audioResource, audioTranscriptionOptions);

    return aiAudioTranscriptionModel.call(prompt).getResult().getOutput();

  }
}
