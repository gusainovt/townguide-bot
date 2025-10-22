package io.project.townguidebot.dto;

import lombok.Data;

@Data
public class Weather {
    private WeatherMain main;
    private WeatherWind wind;
}
