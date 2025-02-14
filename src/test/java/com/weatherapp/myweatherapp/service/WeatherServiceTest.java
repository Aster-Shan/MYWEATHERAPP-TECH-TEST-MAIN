package com.weatherapp.myweatherapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.repository.VisualcrossingRepository;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceTest {

    @Mock
    private VisualcrossingRepository weatherRepo;

    @InjectMocks
    private WeatherService weatherService;

    private CityInfo createMockCityInfo(String sunrise, String sunset, String conditions) {
        CityInfo cityInfo = new CityInfo();
        CityInfo.CurrentConditions currentConditions = new CityInfo.CurrentConditions();
        currentConditions.sunrise = sunrise;
        currentConditions.sunset = sunset;
        currentConditions.conditions = conditions;
        cityInfo.currentConditions = currentConditions;
        return cityInfo;
    }
    
//Testing for daylight comparison
    @Test
    void compareDaylightHours_FirstCityLonger() {
       
        CityInfo london = createMockCityInfo("05:00:00", "21:00:00", "Clear");
        CityInfo newYork = createMockCityInfo("06:00:00", "20:00:00", "Clear");
        
        when(weatherRepo.getByCity("London")).thenReturn(london);
        when(weatherRepo.getByCity("New York")).thenReturn(newYork);

     
        String result = weatherService.compareDaylightHours("London", "New York");
        assertTrue(result.contains("London has longer daylight hours"));
        assertTrue(result.contains("16 hours and 0 minutes"));
        assertTrue(result.contains("14 hours and 0 minutes"));
    }

    @Test
    void compareDaylightHours_SecondCityLonger() {
        CityInfo london = createMockCityInfo("06:00:00", "20:00:00", "Clear");
        CityInfo sydney = createMockCityInfo("05:00:00", "21:00:00", "Clear");
        
        when(weatherRepo.getByCity("London")).thenReturn(london);
        when(weatherRepo.getByCity("Sydney")).thenReturn(sydney);
        String result = weatherService.compareDaylightHours("London", "Sydney");
        assertTrue(result.contains("Sydney has longer daylight hours"));
        assertTrue(result.contains("16 hours and 0 minutes"));
        assertTrue(result.contains("14 hours and 0 minutes"));
    }

    @Test
    void compareDaylightHours_EqualDaylight() {
        CityInfo london = createMockCityInfo("06:00:00", "20:00:00", "Clear");
        CityInfo paris = createMockCityInfo("06:00:00", "20:00:00", "Clear");
        
        when(weatherRepo.getByCity("London")).thenReturn(london);
        when(weatherRepo.getByCity("Paris")).thenReturn(paris);
        String result = weatherService.compareDaylightHours("London", "Paris");
        assertTrue(result.contains("Both cities have the same daylight hours"));
        assertTrue(result.contains("14 hours and 0 minutes"));
    }

    //Testing for rain checking
    @Test
    void checkRain_BothCitiesRaining() {
        CityInfo london = createMockCityInfo("06:00:00", "20:00:00", "Rain");
        CityInfo paris = createMockCityInfo("06:00:00", "20:00:00", "Heavy Rain");
        
        when(weatherRepo.getByCity("London")).thenReturn(london);
        when(weatherRepo.getByCity("Paris")).thenReturn(paris);
        String result = weatherService.checkRain("London", "Paris");
        assertTrue(result.contains("It is raining in both"));
    }

    @Test
    void checkRain_FirstCityRaining() {
        CityInfo london = createMockCityInfo("06:00:00", "20:00:00", "Rain");
        CityInfo paris = createMockCityInfo("06:00:00", "20:00:00", "Clear");
        
        when(weatherRepo.getByCity("London")).thenReturn(london);
        when(weatherRepo.getByCity("Paris")).thenReturn(paris);
        String result = weatherService.checkRain("London", "Paris");
        assertEquals("It is raining in London", result);
    }

    @Test
    void checkRain_SecondCityRaining() {
       
        CityInfo london = createMockCityInfo("06:00:00", "20:00:00", "Clear");
        CityInfo paris = createMockCityInfo("06:00:00", "20:00:00", "Rain");
        
        when(weatherRepo.getByCity("London")).thenReturn(london);
        when(weatherRepo.getByCity("Paris")).thenReturn(paris);
        String result = weatherService.checkRain("London", "Paris");

        assertEquals("It is raining in Paris", result);
    }

    @Test
    void checkRain_NoCityRaining() {
       
        CityInfo london = createMockCityInfo("06:00:00", "20:00:00", "Clear");
        CityInfo paris = createMockCityInfo("06:00:00", "20:00:00", "Cloudy");
        
        when(weatherRepo.getByCity("London")).thenReturn(london);
        when(weatherRepo.getByCity("Paris")).thenReturn(paris);
        String result = weatherService.checkRain("London", "Paris");

        assertEquals("It is not currently raining in both cities", result);
    }

    @Test
    void checkRain_DrizzleCondition() {
    
        CityInfo london = createMockCityInfo("06:00:00", "20:00:00", "Drizzle");
        CityInfo paris = createMockCityInfo("06:00:00", "20:00:00", "Clear");
        
        when(weatherRepo.getByCity("London")).thenReturn(london);
        when(weatherRepo.getByCity("Paris")).thenReturn(paris);
        String result = weatherService.checkRain("London", "Paris");

        assertEquals("It is raining in London", result);
    }

    @Test
    void forecastByCity_Success() {
      
        CityInfo london = createMockCityInfo("06:00:00", "20:00:00", "Clear");
        when(weatherRepo.getByCity("London")).thenReturn(london);
        CityInfo result = weatherService.forecastByCity("London");
        assertNotNull(result);
        assertEquals("06:00:00", result.getCurrentConditions().sunrise);
        assertEquals("20:00:00", result.getCurrentConditions().sunset);
        assertEquals("Clear", result.getCurrentConditions().conditions);
    }
}

