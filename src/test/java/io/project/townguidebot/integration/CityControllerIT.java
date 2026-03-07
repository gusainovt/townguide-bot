package io.project.townguidebot.integration;

import io.project.townguidebot.dto.request.CityCreateRq;
import io.project.townguidebot.dto.response.CityResponse;
import io.project.townguidebot.model.City;
import io.project.townguidebot.repository.CityRepository;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CityControllerIT extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CityRepository cityRepository;

    @BeforeEach
    void clean() {
        cityRepository.deleteAll();
    }

    @Test
    void findAllCities_WhenNoCities_ShouldReturnEmptyArray() {
        ResponseEntity<CityResponse[]> resp = restTemplate.getForEntity("/api/v1/city", CityResponse[].class);

        assertEquals(200, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertEquals(0, resp.getBody().length);
    }

    @Test
    void findAllCities_ShouldReturnInsertedCityWithMappedFields() {
        City city = new City();
        city.setName("Moscow");
        city.setNameEng("moscow");
        city.setDescription("Capital");
        city.setCallback("CITY:moscow");
        city.setPhoto("http://example.com/city.png");
        cityRepository.save(city);

        ResponseEntity<CityResponse[]> resp = restTemplate.getForEntity("/api/v1/city", CityResponse[].class);

        assertEquals(200, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertEquals(1, resp.getBody().length);

        CityResponse body = resp.getBody()[0];
        assertNotNull(body.getId());
        assertEquals("Moscow", body.getName());
        assertEquals("moscow", body.getNameEng());
        assertEquals("Capital", body.getDescription());
        assertEquals("CITY:moscow", body.getCallback());
        assertEquals("http://example.com/city.png", body.getPhoto());
    }

    @Test
    void findAllCities_WhenMultipleCities_ShouldReturnAll() {
        City city1 = new City();
        city1.setName("Moscow");
        city1.setNameEng("moscow");
        city1.setDescription("Capital");
        city1.setCallback("CITY:moscow");
        cityRepository.save(city1);

        City city2 = new City();
        city2.setName("Kazan");
        city2.setNameEng("kazan");
        city2.setDescription("City on the Volga");
        city2.setCallback("CITY:kazan");
        city2.setPhoto(null);
        cityRepository.save(city2);

        ResponseEntity<CityResponse[]> resp = restTemplate.getForEntity("/api/v1/city", CityResponse[].class);

        assertEquals(200, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertEquals(2, resp.getBody().length);

        Map<String, CityResponse> byNameEng = Arrays.stream(resp.getBody())
                .collect(Collectors.toMap(CityResponse::getNameEng, Function.identity()));

        assertTrue(byNameEng.containsKey("moscow"));
        assertTrue(byNameEng.containsKey("kazan"));
        assertNull(byNameEng.get("kazan").getPhoto());
    }

    @Test
    void create_ShouldPersistCityAndReturnMappedResponse() {
        CityCreateRq req = new CityCreateRq();
        req.setName("Borovsk");
        req.setNameEng("borovsk");
        req.setDescription("Historic town");

        ResponseEntity<CityResponse> resp = restTemplate.postForEntity("/api/v1/city", req, CityResponse.class);

        assertEquals(200, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertNotNull(resp.getBody().getId());
        assertEquals("Borovsk", resp.getBody().getName());
        assertEquals("borovsk", resp.getBody().getNameEng());
        assertEquals("Historic town", resp.getBody().getDescription());
        assertEquals("CITY:borovsk", resp.getBody().getCallback());
        assertEquals(1, cityRepository.count());

        City savedCity = cityRepository.findAll().getFirst();
        assertEquals("Borovsk", savedCity.getName());
        assertEquals("borovsk", savedCity.getNameEng());
        assertEquals("Historic town", savedCity.getDescription());
        assertEquals("CITY:borovsk", savedCity.getCallback());
        assertNull(savedCity.getPhoto());
    }
}
