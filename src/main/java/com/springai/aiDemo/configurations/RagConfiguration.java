package com.springai.aiDemo.configurations;

import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Configuration
public class RagConfiguration {

  @Value("classpath:/docs/olympic-faq.txt")
  private Resource resource;

  @Value("vectorstore.json")
  private String vectorStoreName;

  @Bean
  public SimpleVectorStore getVectorStore(OpenAiEmbeddingModel openAiEmbeddingModel){
    SimpleVectorStore vectorStore = new SimpleVectorStore(openAiEmbeddingModel);
    File vectorstoreFile = getVectorStoreFile();

    if(!vectorstoreFile.exists()){
      TextReader textReader = new TextReader(resource);
      textReader.getCustomMetadata().put("fileName", "olympic-faq.txt");
      List<Document> documents = textReader.get();
      TextSplitter splitter = new TokenTextSplitter();
      List<Document> splitDocuments = splitter.apply(documents);
      vectorStore.add(splitDocuments);
      vectorStore.save(vectorstoreFile);
    }else{
      vectorStore.load(vectorstoreFile);
    }
    return vectorStore;
  }

  private File getVectorStoreFile(){
    Path path = Paths.get("src", "main", "resources", "data");
    return new File(path.toFile().getAbsolutePath()+"/"+vectorStoreName);
  }
}
