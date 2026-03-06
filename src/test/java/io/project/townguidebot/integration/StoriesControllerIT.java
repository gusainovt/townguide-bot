package io.project.townguidebot.integration;

import io.project.townguidebot.dto.StoryDto;
import io.project.townguidebot.exception.dto.ErrorResponse;
import io.project.townguidebot.model.City;
import io.project.townguidebot.model.Story;
import io.project.townguidebot.repository.CityRepository;
import io.project.townguidebot.repository.StoryRepository;
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

class StoriesControllerIT extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private CityRepository cityRepository;

    @BeforeEach
    void clean() {
        storyRepository.deleteAll();
        cityRepository.deleteAll();
    }

    @Test
    void findAllStories_WhenNoStories_ShouldReturnEmptyList() {
        ResponseEntity<List<StoryDto>> resp = restTemplate.exchange(
                "/api/v1/stories",
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
    void findAllStories_ShouldReturnInsertedStory() {
        City city = new City();
        city.setName("Moscow");
        city.setNameEng("moscow");
        city.setDescription("Capital");
        city.setCallback("CITY:moscow");
        City savedCity = cityRepository.save(city);

        Story story = new Story();
        story.setBody("Hello story");
        story.setCity(savedCity);
        storyRepository.save(story);

        ResponseEntity<List<StoryDto>> resp = restTemplate.exchange(
                "/api/v1/stories",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(200, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertEquals(1, resp.getBody().size());
        assertEquals("Hello story", resp.getBody().getFirst().getBody());
        assertEquals(savedCity.getId(), resp.getBody().getFirst().getCityId());
    }

    @Test
    void getStoryById_ShouldReturnInsertedStory() {
        City city = new City();
        city.setName("Moscow");
        city.setNameEng("moscow");
        city.setDescription("Capital");
        city.setCallback("CITY:moscow");
        City savedCity = cityRepository.save(city);

        Story story = new Story();
        story.setBody("Get by id");
        story.setCity(savedCity);
        Story savedStory = storyRepository.save(story);

        ResponseEntity<StoryDto> resp = restTemplate.getForEntity("/api/v1/stories/" + savedStory.getId(), StoryDto.class);

        assertEquals(200, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertEquals("Get by id", resp.getBody().getBody());
        assertEquals(savedCity.getId(), resp.getBody().getCityId());
    }

    @Test
    void getStoryById_WhenMissing_ShouldReturn404WithErrorResponse() {
        ResponseEntity<ErrorResponse> resp = restTemplate.getForEntity("/api/v1/stories/999999", ErrorResponse.class);

        assertEquals(404, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertEquals("STORY_NOT_FOUND", resp.getBody().getType().name());
        assertTrue(resp.getBody().getMessage().contains("999999"));
        assertNotNull(resp.getBody().getTimestamp());
    }

    @Test
    void createStory_ShouldPersistAndReturnCreated() {
        City city = new City();
        city.setName("Moscow");
        city.setNameEng("moscow");
        city.setDescription("Capital");
        city.setCallback("CITY:moscow");
        City savedCity = cityRepository.save(city);

        StoryDto req = new StoryDto();
        req.setCityId(savedCity.getId());
        req.setBody("Created via API");

        ResponseEntity<StoryDto> resp = restTemplate.postForEntity("/api/v1/stories", req, StoryDto.class);

        assertEquals(201, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertNotNull(resp.getBody().getId());
        assertEquals(savedCity.getId(), resp.getBody().getCityId());
        assertEquals("Created via API", resp.getBody().getBody());
        assertEquals(1, storyRepository.count());
    }

    @Test
    void updateStory_ShouldPersistAndReturnUpdated() {
        City city = new City();
        city.setName("Moscow");
        city.setNameEng("moscow");
        city.setDescription("Capital");
        city.setCallback("CITY:moscow");
        City savedCity = cityRepository.save(city);

        Story story = new Story();
        story.setBody("Old");
        story.setCity(savedCity);
        Story savedStory = storyRepository.save(story);

        StoryDto req = new StoryDto();
        req.setCityId(savedCity.getId());
        req.setBody("New");

        ResponseEntity<StoryDto> resp = restTemplate.exchange(
                "/api/v1/stories/" + savedStory.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(req),
                StoryDto.class
        );

        assertEquals(200, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertEquals("New", resp.getBody().getBody());
        assertEquals("New", storyRepository.findById(savedStory.getId()).orElseThrow().getBody());
    }

    @Test
    void updateStory_WhenMissing_ShouldReturn404WithErrorResponse() {
        City city = new City();
        city.setName("Moscow");
        city.setNameEng("moscow");
        city.setDescription("Capital");
        city.setCallback("CITY:moscow");
        City savedCity = cityRepository.save(city);

        StoryDto req = new StoryDto();
        req.setCityId(savedCity.getId());
        req.setBody("New");

        ResponseEntity<ErrorResponse> resp = restTemplate.exchange(
                "/api/v1/stories/999999",
                HttpMethod.PUT,
                new HttpEntity<>(req),
                ErrorResponse.class
        );

        assertEquals(404, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertEquals("STORY_NOT_FOUND", resp.getBody().getType().name());
        assertTrue(resp.getBody().getMessage().contains("999999"));
    }

    @Test
    void deleteStory_ShouldRemoveStory() {
        City city = new City();
        city.setName("Moscow");
        city.setNameEng("moscow");
        city.setDescription("Capital");
        city.setCallback("CITY:moscow");
        City savedCity = cityRepository.save(city);

        Story story = new Story();
        story.setBody("To delete");
        story.setCity(savedCity);
        Story savedStory = storyRepository.save(story);

        ResponseEntity<Void> resp = restTemplate.exchange(
                "/api/v1/stories/" + savedStory.getId(),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class
        );

        assertEquals(204, resp.getStatusCode().value());
        assertEquals(0, storyRepository.count());
    }

    @Test
    void deleteStory_WhenMissing_ShouldReturn404WithErrorResponse() {
        ResponseEntity<ErrorResponse> resp = restTemplate.exchange(
                "/api/v1/stories/999999",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                ErrorResponse.class
        );

        assertEquals(404, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertEquals("STORY_NOT_FOUND", resp.getBody().getType().name());
        assertTrue(resp.getBody().getMessage().contains("999999"));
    }

    @Test
    void getRandomStory_ShouldReturnStoryForCity() {
        City city = new City();
        city.setName("Moscow");
        city.setNameEng("moscow");
        city.setDescription("Capital");
        city.setCallback("CITY:moscow");
        City savedCity = cityRepository.save(city);

        Story story = new Story();
        story.setBody("Random story");
        story.setCity(savedCity);
        storyRepository.save(story);

        ResponseEntity<StoryDto> resp = restTemplate.getForEntity("/api/v1/stories/moscow/random", StoryDto.class);

        assertEquals(200, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertEquals("Random story", resp.getBody().getBody());
        assertEquals(savedCity.getId(), resp.getBody().getCityId());
    }

    @Test
    void getRandomStory_WhenMissing_ShouldReturn404WithErrorResponse() {
        ResponseEntity<ErrorResponse> resp = restTemplate.getForEntity("/api/v1/stories/moscow/random", ErrorResponse.class);

        assertEquals(404, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertEquals("STORY_NOT_FOUND", resp.getBody().getType().name());
        assertNotNull(resp.getBody().getMessage());
        assertNotNull(resp.getBody().getTimestamp());
    }
}

