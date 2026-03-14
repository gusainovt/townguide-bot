package io.project.townguidebot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherWind {
    private BigDecimal speed;
    private Integer deg;
}
