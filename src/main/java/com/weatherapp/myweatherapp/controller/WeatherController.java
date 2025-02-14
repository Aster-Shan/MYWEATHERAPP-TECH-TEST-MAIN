package com.weatherapp.myweatherapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.service.WeatherService;

@Controller
public class WeatherController {

  @Autowired
  WeatherService weatherService;

  @GetMapping("/forecast/{city}")
  public ResponseEntity<?> forecastByCity(@PathVariable("city") String city) {
      try {
          CityInfo cityInfo = weatherService.forecastByCity(city);
          return ResponseEntity.ok(cityInfo);
      } catch (Exception e) {
          return ResponseEntity.status(500).body("Error fetching forecast for " + city + ": " + e.getMessage());
      }
  }
  public String getMethodName(@RequestParam String param) {
      return new String();
  }
  
  //API for to call compare-daylight service
  @GetMapping("/compare-daylight")
    public ResponseEntity<String> compareDaylightHours(
            @RequestParam("city1") String city1,
            @RequestParam("city2") String city2) {
        try {
            String result = weatherService.compareDaylightHours(city1, city2);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error comparing daylight hours: " + e.getMessage());
        }
    }

  //API for to call check-rain service
    @GetMapping("/check-rain")
    public ResponseEntity<String> checkRain(
            @RequestParam("city1") String city1,
            @RequestParam("city2") String city2) {
        try {
            String result = weatherService.checkRain(city1, city2);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error checking rain: " + e.getMessage());
        }
    }


}
