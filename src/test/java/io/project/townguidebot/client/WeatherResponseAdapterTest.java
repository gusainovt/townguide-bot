package io.project.townguidebot.client;

import io.project.townguidebot.dto.Weather;
import io.project.townguidebot.dto.WeatherInfo;
import io.project.townguidebot.dto.WeatherMain;
import io.project.townguidebot.dto.WeatherWind;
import io.project.townguidebot.exception.WeatherIntegrationException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WeatherResponseAdapterTest {

    private final WeatherResponseAdapter adapter = new WeatherResponseAdapter();

    @Test
    void adapt_ShouldFallbackFeelsLikeToTemp_WhenFeelsLikeIsMissing() {
        WeatherMain main = new WeatherMain();
        main.setTemp(BigDecimal.valueOf(10.5));

        WeatherWind wind = new WeatherWind();
        wind.setSpeed(BigDecimal.valueOf(3.4));

        Weather weather = new Weather();
        weather.setMain(main);
        weather.setWind(wind);

        WeatherInfo result = adapter.adapt(weather, "moscow");

        assertEquals(BigDecimal.valueOf(10.5), result.getTemp());
        assertEquals(BigDecimal.valueOf(10.5), result.getFeelsLike());
        assertEquals(BigDecimal.valueOf(3.4), result.getWindSpeed());
    }

    @Test
    void adapt_ShouldThrow_WhenMainTempIsMissing() {
        Weather weather = new Weather();
        weather.setMain(new WeatherMain());
        weather.setWind(new WeatherWind());

        assertThrows(WeatherIntegrationException.class, () -> adapter.adapt(weather, "moscow"));
    }
}
