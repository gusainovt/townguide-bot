package io.project.townguidebot.controller.impl;

import gusainov.library.annotation.LogUserRequest;
import io.project.townguidebot.controller.StoriesController;
import io.project.townguidebot.dto.StoryDto;
import io.project.townguidebot.dto.request.StoryRq;
import io.project.townguidebot.service.StoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StoriesControllerImpl implements StoriesController {

    private final StoryService storyService;

    @Override
    @LogUserRequest(method = "findAllStories")
    public ResponseEntity<List<StoryDto>> findAllStories() {
        return ResponseEntity.ok(storyService.findAllStories());
    }

    @Override
    @LogUserRequest(method = "getStoryById")
    public ResponseEntity<StoryDto> getStoryById(Long id) {
        return ResponseEntity.ok(storyService.findStoryById(id));
    }

    @Override
    @LogUserRequest(method = "createStory")
    public ResponseEntity<StoryDto> createStory(StoryRq req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(storyService.createStory(req));
    }

    @Override
    @LogUserRequest(method = "updateStory")
    public ResponseEntity<StoryDto> updateStory(long id, StoryDto storyDto) {
        return ResponseEntity.ok(storyService.updateStory(id, storyDto));
    }

    @Override
    @LogUserRequest(method = "deleteStory")
    public ResponseEntity<Void> deleteStory(long id) {
        storyService.deleteStory(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @LogUserRequest(method = "getRandomStory")
    public ResponseEntity<StoryDto> getRandomStory(String cityName) {
        return ResponseEntity.ok(storyService.getRandomStoryForCity(cityName));
    }

}
