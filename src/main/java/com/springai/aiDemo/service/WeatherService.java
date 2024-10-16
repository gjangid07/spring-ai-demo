package com.springai.aiDemo.service;

import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.function.Function;

@Service
@Description("weatherService")
public class WeatherService implements Function<WeatherService.Request, WeatherService.Response> {

  private RestClient restClient;

  public WeatherService() {
    this.restClient = RestClient.create("http://api.weatherapi.com");
  }

  @Override
  public Response apply(Request request){
    return this.restClient.get()
        .uri("/v1/current.json?key={key}&q={city}","0772d468c4fc43b0bd081115240610", request.city)
        .retrieve()
        .body(Response.class);
  }

  public record Request(String city){}
  public record Response(Location location, Current current){}
  public record Location(String name, String region, String country, long lat, long lon){}
  public record Current(String temp_c, Condition condition){}
  public record Condition(String text){}

}
