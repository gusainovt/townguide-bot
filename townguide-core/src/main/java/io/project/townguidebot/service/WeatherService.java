package io.project.townguidebot.service;

import io.project.townguidebot.dto.WeatherInfo;

public interface WeatherService {
    WeatherInfo getWeather(String city);
}

