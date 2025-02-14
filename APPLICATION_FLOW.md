# Application Flow

This document describes the detailed flow of the Weather Comparison Application.

## 1. User Interaction

1. User opens the application in a web browser.
2. The `HomeController` serves the `home.html` page.
3. User sees two options:
   - Compare Daylight Hours
   - Check Rain
4. User selects an option and enters two city names.
5. User submits the form.

## 2. Request Handling

1. The form submission sends a GET request to the appropriate endpoint:
   - `/compare-daylight` for daylight comparison
   - `/check-rain` for rain checking
2. The `WeatherController` receives the request with city names as query parameters.

## 3. Service Layer Processing

1. The `WeatherController` calls the appropriate method in `WeatherService`:
   - `compareDaylightHours()` for daylight comparison
   - `checkRain()` for rain checking
2. The `WeatherService` processes the request:
   
   For daylight comparison:
   a. Calls `VisualcrossingRepository.getByCity()` for both cities.
   b. Extracts sunrise and sunset times from the API response.
   c. Calculates daylight duration using `calculateDayLightHours()`.
   d. Compares durations and formats the result.

   For rain checking:
   a. Calls `VisualcrossingRepository.getByCity()` for both cities.
   b. Checks weather conditions using `isRaining()` method.
   c. Determines which city (if any) is experiencing rain.

## 4. Data Fetching

1. `VisualcrossingRepository.getByCity()` is called for each city.
2. The repository constructs the API URL using the base URL and API key from `application.properties`.
3. It sends an HTTP GET request to the Visual Crossing Weather API.
4. The API responds with JSON data.
5. Spring's `RestTemplate` automatically deserializes the JSON into a `CityInfo` object.

## 5. Response Handling

1. The `WeatherService` returns the processed result as a String.
2. The `WeatherController` wraps this result in a `ResponseEntity`.
3. Spring MVC converts the `ResponseEntity` to an HTTP response.
4. The response is sent back to the user's browser.

## 6. Error Handling

- If an exception occurs during processing, it's caught in the `WeatherController`.
- The controller returns an error message wrapped in a `ResponseEntity` with a BAD_REQUEST status.
- If a request is made to an undefined endpoint, the `CustomErrorController` handles it and returns a custom error message.

## 7. Display Results

1. The browser receives the HTTP response.
2. The result is displayed to the user on the same page.

This flow ensures a separation of concerns, with each component handling its specific responsibilities, resulting in a modular and maintainable application structure.

