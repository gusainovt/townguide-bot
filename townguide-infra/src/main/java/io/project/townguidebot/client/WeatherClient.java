package io.project.townguidebot.client;

import io.project.townguidebot.dto.Weather;
import io.project.townguidebot.dto.WeatherInfo;
import io.project.townguidebot.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherClient implements WeatherService {

    @Value("${weather-forecast-service.url}")
    private String url;

    @Value("${weather-forecast-service.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final WeatherResponseAdapter weatherResponseAdapter;

    /**
     * Возвращает погоду по названию города
     * @param city название города
     * @return объект {@link Weather}
     */
    @Override
    public WeatherInfo getWeather(String city) {
        log.debug("Get weather for city: {}", city);
        Weather weather = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(HttpHeaders.EMPTY),
                Weather.class, city, apiKey
        ).getBody();
        return weatherResponseAdapter.adapt(weather, city);
    }
}
