package io.project.townguidebot.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherMain {
    private BigDecimal temp;
    private BigDecimal humidity;

    @JsonAlias({"feels_like", "feelsLike"})
    private BigDecimal feelsLike;

}
