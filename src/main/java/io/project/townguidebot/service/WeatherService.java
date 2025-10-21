package io.project.townguidebot.service;

import io.project.townguidebot.model.dto.Weather;

public interface WeatherService {
    Weather getWeather(String city);
}
