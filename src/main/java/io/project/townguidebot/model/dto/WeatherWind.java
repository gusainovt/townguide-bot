package io.project.townguidebot.model.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class WeatherWind {
    private BigDecimal speed;
    private Integer deg;
}
