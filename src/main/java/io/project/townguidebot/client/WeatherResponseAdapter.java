package io.project.townguidebot.client;

import io.project.townguidebot.dto.Weather;
import io.project.townguidebot.dto.WeatherInfo;
import io.project.townguidebot.dto.WeatherMain;
import io.project.townguidebot.dto.WeatherWind;
import io.project.townguidebot.exception.WeatherIntegrationException;
import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WeatherResponseAdapter {

    public WeatherInfo adapt(Weather weather, String city) {
        if (weather == null) {
            throw new WeatherIntegrationException("Weather response is empty for city: " + city);
        }

        WeatherMain main = weather.getMain();
        if (main == null || main.getTemp() == null) {
            throw new WeatherIntegrationException("Weather main data is invalid for city: " + city);
        }

        WeatherWind wind = weather.getWind();
        if (wind == null || wind.getSpeed() == null) {
            throw new WeatherIntegrationException("Weather wind data is invalid for city: " + city);
        }

        BigDecimal feelsLike = main.getFeelsLike();
        if (feelsLike == null) {
            feelsLike = main.getTemp();
            log.warn("Weather API returned null feelsLike for city: {}. Fallback to temp={}", city, main.getTemp());
        }

        return WeatherInfo.builder()
                .temp(main.getTemp())
                .feelsLike(feelsLike)
                .windSpeed(wind.getSpeed())
                .build();
    }
}
