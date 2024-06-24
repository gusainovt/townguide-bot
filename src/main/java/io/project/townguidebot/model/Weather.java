package io.project.townguidebot.model;

import lombok.Data;

@Data
public class Weather {
    private WeatherMain main;
    private WeatherWind wind;
}
