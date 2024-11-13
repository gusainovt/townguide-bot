package io.project.BorovskBot.service;

import io.project.BorovskBot.model.Weather;

public interface WeatherService {
    Weather getWeather(String city);
}
