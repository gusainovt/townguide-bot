package io.project.townguidebot.integration;

import io.project.townguidebot.dto.AdDto;
import io.project.townguidebot.exception.dto.ErrorResponse;
import io.project.townguidebot.model.Ad;
import io.project.townguidebot.repository.AdsRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdsControllerIT extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AdsRepository adsRepository;

    @BeforeEach
    void clean() {
        adsRepository.deleteAll();
    }

    @Test
    void findAllAds_WhenNoAds_ShouldReturnEmptyList() {
        ResponseEntity<List<AdDto>> resp = restTemplate.exchange(
                "/api/v1/ads",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(200, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertTrue(resp.getBody().isEmpty());
    }

    @Test
    void findAllAds_ShouldReturnInsertedAd() {
        Ad ad = new Ad();
        ad.setAd("Hello from IT");
        adsRepository.save(ad);

        ResponseEntity<List<AdDto>> resp = restTemplate.exchange(
                "/api/v1/ads",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(200, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertEquals(1, resp.getBody().size());
        assertEquals("Hello from IT", resp.getBody().getFirst().getAd());
    }

    @Test
    void getAdById_ShouldReturnInsertedAd() {
        Ad ad = new Ad();
        ad.setAd("Get by id");
        Ad saved = adsRepository.save(ad);

        ResponseEntity<AdDto> resp = restTemplate.getForEntity("/api/v1/ads/" + saved.getId(), AdDto.class);

        assertEquals(200, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertEquals("Get by id", resp.getBody().getAd());
    }

    @Test
    void getAdById_WhenMissing_ShouldReturn404WithErrorResponse() {
        ResponseEntity<ErrorResponse> resp = restTemplate.getForEntity("/api/v1/ads/999999", ErrorResponse.class);

        assertEquals(404, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertEquals("AD_NOT_FOUND", resp.getBody().getType().name());
        assertNotNull(resp.getBody().getMessage());
        assertTrue(resp.getBody().getMessage().contains("999999"));
        assertNotNull(resp.getBody().getTimestamp());
    }

    @Test
    void createAd_ShouldPersistAndReturnCreated() {
        AdDto req = new AdDto();
        req.setAd("Created via API");

        ResponseEntity<AdDto> resp = restTemplate.postForEntity("/api/v1/ads", req, AdDto.class);

        assertEquals(201, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertEquals("Created via API", resp.getBody().getAd());
        assertEquals(1, adsRepository.count());
        assertEquals("Created via API", adsRepository.findAll().getFirst().getAd());
    }

    @Test
    void updateAd_ShouldPersistAndReturnUpdated() {
        Ad ad = new Ad();
        ad.setAd("Old");
        Ad saved = adsRepository.save(ad);

        AdDto req = new AdDto();
        req.setAd("New");

        ResponseEntity<AdDto> resp = restTemplate.exchange(
                "/api/v1/ads/" + saved.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(req),
                AdDto.class
        );

        assertEquals(200, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertEquals("New", resp.getBody().getAd());
        assertEquals("New", adsRepository.findById(saved.getId()).orElseThrow().getAd());
    }

    @Test
    void updateAd_WhenMissing_ShouldReturn404WithErrorResponse() {
        AdDto req = new AdDto();
        req.setAd("New");

        ResponseEntity<ErrorResponse> resp = restTemplate.exchange(
                "/api/v1/ads/999999",
                HttpMethod.PUT,
                new HttpEntity<>(req),
                ErrorResponse.class
        );

        assertEquals(404, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertEquals("AD_NOT_FOUND", resp.getBody().getType().name());
        assertNotNull(resp.getBody().getMessage());
        assertTrue(resp.getBody().getMessage().contains("999999"));
        assertNotNull(resp.getBody().getTimestamp());
    }

    @Test
    void deleteAd_ShouldRemoveAd() {
        Ad ad = new Ad();
        ad.setAd("To delete");
        Ad saved = adsRepository.save(ad);

        ResponseEntity<Void> resp = restTemplate.exchange(
                "/api/v1/ads/" + saved.getId(),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class
        );

        assertEquals(204, resp.getStatusCode().value());
        assertEquals(0, adsRepository.count());
    }

    @Test
    void deleteAd_WhenMissing_ShouldReturn404WithErrorResponse() {
        assertEquals(0, adsRepository.count());

        ResponseEntity<ErrorResponse> resp = restTemplate.exchange(
                "/api/v1/ads/999999",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                ErrorResponse.class
        );

        assertEquals(404, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertEquals("AD_NOT_FOUND", resp.getBody().getType().name());
        assertNotNull(resp.getBody().getMessage());
        assertTrue(resp.getBody().getMessage().contains("999999"));
        assertNotNull(resp.getBody().getTimestamp());
        assertEquals(0, adsRepository.count());
    }
}
