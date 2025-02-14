package com.weatherapp.myweatherapp.service;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.repository.VisualcrossingRepository;
@Service
public class WeatherService {

  @Autowired
  VisualcrossingRepository weatherRepo;

  public CityInfo forecastByCity(String city) {

    return weatherRepo.getByCity(city);
  }

  //Calculate daylight of cities to compare
  private Duration CalculateDayLightHours (CityInfo cityinfo){
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    LocalTime sunrise =LocalTime.parse(cityinfo.getCurrentConditions().sunrise,formatter);
    LocalTime sunset = LocalTime.parse(cityinfo.getCurrentConditions().sunset,formatter);
    return Duration.between(sunrise, sunset);
  }

  private String formatDuration(Duration duration){
    long hours = duration.toHours();
    long minutes =duration.toMinutesPart();
    return String.format("%d hours and %d minutes", hours, minutes);
  }

  //Compare day-light between two cities
  public String compareDaylightHours(String city1,String city2){
    CityInfo Info1 = weatherRepo.getByCity(city1);
    CityInfo Info2 = weatherRepo.getByCity(city2);

    Duration Daylight1= CalculateDayLightHours(Info1);
    Duration Daylight2= CalculateDayLightHours(Info2);

    if (Daylight1.compareTo(Daylight2) > 0) {
        return city1 + " has longer daylight hours.<br>" +
               "The daylight hour of " + city1 + " is: " + formatDuration(Daylight1) + "<br>" +
               "The daylight hour of " + city2 + " is: " + formatDuration(Daylight2);
    } else if (Daylight1.compareTo(Daylight2) < 0) {
        return city2 + " has longer daylight hours.<br>" +
               "The daylight hour of " + city2 + " is: " + formatDuration(Daylight2) + "<br>" +
               "The daylight hour of " + city1 + " is: " + formatDuration(Daylight1);
    } else {
        return "Both cities have the same daylight hours.<br>" +
               "The daylight hour of both cities is: " + formatDuration(Daylight1);
    }

  }

 
  private boolean isRaining(CityInfo cityinfo){
    String conditions = cityinfo.getCurrentConditions().conditions.toLowerCase();
    return conditions.contains("rain")|| conditions.contains("drizzle") || conditions.contains("shower");
  }

  //Rain Check
  public String checkRain(String city1, String city2){
    CityInfo Info1 = weatherRepo.getByCity(city1);
    CityInfo Info2 = weatherRepo.getByCity(city2);

    boolean isRaining1 = isRaining(Info1);
    boolean isRaining2 = isRaining(Info2);

    if(isRaining1 && isRaining2){
      return "It is raining in both " + city1 + "and " + city2;
    }
    else if(isRaining1){
      return "It is raining in " + city1 ;
    }
    else if (isRaining2){
      return "It is raining in " + city2 ;
    }
    else {
      return "It is not currently raining in both cities";
    }
  }

}
