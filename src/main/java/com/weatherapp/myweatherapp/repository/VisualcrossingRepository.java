package com.weatherapp.myweatherapp.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.weatherapp.myweatherapp.model.CityInfo;

@Repository
public class VisualcrossingRepository {

  @Value("${weather.visualcrossing.url}")
  String url;
  @Value("${weather.visualcrossing.key}")
  String key;


  public CityInfo getByCity(String city) {
    String uri = UriComponentsBuilder
        .fromUriString(url)
        .path("timeline/")
        .path(city)
        .queryParam("key", key)
        .queryParam("unitGroup", "metric")
        .queryParam("contentType", "json")
        .build()
        .toUriString();
        
    try {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(uri, CityInfo.class);
    } catch (Exception e) {
        throw new RuntimeException("Error fetching weather data for " + city, e);
    }

  }
  
  
}
