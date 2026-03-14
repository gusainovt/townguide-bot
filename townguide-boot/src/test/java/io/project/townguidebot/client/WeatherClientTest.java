package io.project.townguidebot.client;

import io.project.townguidebot.dto.WeatherInfo;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class WeatherClientTest {

    private RestTemplate restTemplate;
    private MockRestServiceServer server;
    private WeatherClient weatherClient;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
        server = MockRestServiceServer.bindTo(restTemplate).build();
        weatherClient = new WeatherClient(restTemplate, new WeatherResponseAdapter());
        ReflectionTestUtils.setField(weatherClient, "url", "http://localhost/weather?city={city}&apiKey={apiKey}");
        ReflectionTestUtils.setField(weatherClient, "apiKey", "test-key");
    }

    @Test
    void getWeather_ShouldMapSnakeCaseFeelsLikeAndNormalizeResponse() {
        server.expect(once(), requestTo("http://localhost/weather?city=moscow&apiKey=test-key"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                        {
                          "main": { "temp": 10.0, "humidity": 50, "feels_like": 8.0 },
                          "wind": { "speed": 3.0, "deg": 10 }
                        }
                        """, MediaType.APPLICATION_JSON));

        WeatherInfo result = weatherClient.getWeather("moscow");

        assertEquals(BigDecimal.valueOf(10.0), result.getTemp());
        assertEquals(BigDecimal.valueOf(8.0), result.getFeelsLike());
        assertEquals(BigDecimal.valueOf(3.0), result.getWindSpeed());
        server.verify();
    }

    @Test
    void getWeather_ShouldFallbackFeelsLikeToTemp_WhenFieldIsMissing() {
        server.expect(once(), requestTo("http://localhost/weather?city=moscow&apiKey=test-key"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                        {
                          "main": { "temp": 10.0, "humidity": 50 },
                          "wind": { "speed": 3.0, "deg": 10 }
                        }
                        """, MediaType.APPLICATION_JSON));

        WeatherInfo result = weatherClient.getWeather("moscow");

        assertEquals(BigDecimal.valueOf(10.0), result.getTemp());
        assertEquals(BigDecimal.valueOf(10.0), result.getFeelsLike());
        assertEquals(BigDecimal.valueOf(3.0), result.getWindSpeed());
        server.verify();
    }
}
