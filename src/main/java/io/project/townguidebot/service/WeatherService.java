package io.project.townguidebot.service;

import io.project.townguidebot.model.Weather;

public interface WeatherService {
    Weather getWeather(String city);
}
