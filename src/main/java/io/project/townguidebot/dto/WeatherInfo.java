package io.project.townguidebot.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class WeatherInfo {
    BigDecimal temp;
    BigDecimal feelsLike;
    BigDecimal windSpeed;
}
