package io.project.townguidebot.model;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class WeatherMain {
    private BigDecimal temp;
    private BigDecimal humidity;
    private BigDecimal feels_like;

}
