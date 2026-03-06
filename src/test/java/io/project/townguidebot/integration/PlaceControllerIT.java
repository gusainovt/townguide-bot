package io.project.townguidebot.integration;

import io.project.townguidebot.dto.PlaceDto;
import io.project.townguidebot.exception.dto.ErrorResponse;
import io.project.townguidebot.model.City;
import io.project.townguidebot.model.Place;
import io.project.townguidebot.repository.CityRepository;
import io.project.townguidebot.repository.PlaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlaceControllerIT extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private CityRepository cityRepository;

    @BeforeEach
    void clean() {
        placeRepository.deleteAll();
        cityRepository.deleteAll();
    }

    @Test
    void getPlaceById_ShouldReturnInsertedPlace() {
        City city = new City();
        city.setName("Moscow");
        city.setNameEng("moscow");
        city.setDescription("Capital");
        city.setCallback("CITY:moscow");
        City savedCity = cityRepository.save(city);

        Place place = new Place();
        place.setName("Red Square");
        place.setDescription("Main square");
        place.setCity(savedCity);
        Place savedPlace = placeRepository.save(place);

        ResponseEntity<PlaceDto> resp = restTemplate.getForEntity("/api/v1/places/" + savedPlace.getId(), PlaceDto.class);

        assertEquals(200, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertEquals(savedPlace.getId(), resp.getBody().getId());
        assertEquals(savedCity.getId(), resp.getBody().getCityId());
        assertEquals("Red Square", resp.getBody().getName());
        assertEquals("Main square", resp.getBody().getDescription());
    }

    @Test
    void getPlaceById_WhenMissing_ShouldReturn404WithErrorResponse() {
        ResponseEntity<ErrorResponse> resp = restTemplate.getForEntity("/api/v1/places/999999", ErrorResponse.class);

        assertEquals(404, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertEquals("PLACE_NOT_FOUND", resp.getBody().getType().name());
        assertTrue(resp.getBody().getMessage().contains("999999"));
        assertNotNull(resp.getBody().getTimestamp());
    }

    @Test
    void createPlace_ShouldPersistAndReturnCreated() {
        City city = new City();
        city.setName("Moscow");
        city.setNameEng("moscow");
        city.setDescription("Capital");
        city.setCallback("CITY:moscow");
        City savedCity = cityRepository.save(city);

        PlaceDto req = new PlaceDto();
        req.setCityId(savedCity.getId());
        req.setName("Red Square");
        req.setDescription("Main square");

        ResponseEntity<PlaceDto> resp = restTemplate.postForEntity("/api/v1/places", req, PlaceDto.class);

        assertEquals(201, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertNotNull(resp.getBody().getId());
        assertEquals(savedCity.getId(), resp.getBody().getCityId());
        assertEquals("Red Square", resp.getBody().getName());
        assertEquals(1, placeRepository.count());
    }

    @Test
    void updatePlace_ShouldPersistAndReturnUpdated() {
        City city = new City();
        city.setName("Moscow");
        city.setNameEng("moscow");
        city.setDescription("Capital");
        city.setCallback("CITY:moscow");
        City savedCity = cityRepository.save(city);

        Place place = new Place();
        place.setName("Old");
        place.setDescription("Old desc");
        place.setCity(savedCity);
        Place savedPlace = placeRepository.save(place);

        PlaceDto req = new PlaceDto();
        req.setCityId(savedCity.getId());
        req.setName("New");
        req.setDescription("New desc");

        ResponseEntity<PlaceDto> resp = restTemplate.exchange(
                "/api/v1/places/" + savedPlace.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(req),
                PlaceDto.class
        );

        assertEquals(200, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertEquals("New", resp.getBody().getName());
        assertEquals("New", placeRepository.findById(savedPlace.getId()).orElseThrow().getName());
    }

    @Test
    void updatePlace_WhenMissing_ShouldReturn404WithErrorResponse() {
        City city = new City();
        city.setName("Moscow");
        city.setNameEng("moscow");
        city.setDescription("Capital");
        city.setCallback("CITY:moscow");
        City savedCity = cityRepository.save(city);

        PlaceDto req = new PlaceDto();
        req.setCityId(savedCity.getId());
        req.setName("New");
        req.setDescription("New desc");

        ResponseEntity<ErrorResponse> resp = restTemplate.exchange(
                "/api/v1/places/999999",
                HttpMethod.PUT,
                new HttpEntity<>(req),
                ErrorResponse.class
        );

        assertEquals(404, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertEquals("PLACE_NOT_FOUND", resp.getBody().getType().name());
        assertTrue(resp.getBody().getMessage().contains("999999"));
    }

    @Test
    void deletePlace_ShouldRemovePlace() {
        City city = new City();
        city.setName("Moscow");
        city.setNameEng("moscow");
        city.setDescription("Capital");
        city.setCallback("CITY:moscow");
        City savedCity = cityRepository.save(city);

        Place place = new Place();
        place.setName("To delete");
        place.setDescription("desc");
        place.setCity(savedCity);
        Place savedPlace = placeRepository.save(place);

        ResponseEntity<Void> resp = restTemplate.exchange(
                "/api/v1/places/" + savedPlace.getId(),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class
        );

        assertEquals(204, resp.getStatusCode().value());
        assertEquals(0, placeRepository.count());
    }

    @Test
    void deletePlace_WhenMissing_ShouldReturn404WithErrorResponse() {
        ResponseEntity<ErrorResponse> resp = restTemplate.exchange(
                "/api/v1/places/999999",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                ErrorResponse.class
        );

        assertEquals(404, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertEquals("PLACE_NOT_FOUND", resp.getBody().getType().name());
        assertTrue(resp.getBody().getMessage().contains("999999"));
    }
}

