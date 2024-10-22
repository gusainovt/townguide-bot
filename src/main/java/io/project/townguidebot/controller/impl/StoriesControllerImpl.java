package io.project.townguidebot.controller.impl;

import io.project.townguidebot.controller.StoriesController;
import io.project.townguidebot.dto.StoryDto;
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
    public ResponseEntity<List<StoryDto>> findAllStories() {
        return ResponseEntity.ok(storyService.findAllStories());
    }

    @Override
    public ResponseEntity<StoryDto> getStoryById(Long id) {
        return ResponseEntity.ok(storyService.findStoryById(id));
    }

    @Override
    public ResponseEntity<StoryDto> createStory(StoryDto storyDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(storyService.createStory(storyDto));
    }

    @Override
    public ResponseEntity<StoryDto> updateStory(long id, StoryDto storyDto) {
        return ResponseEntity.ok(storyService.updateStory(id, storyDto));
    }

    @Override
    public ResponseEntity<Void> deleteStory(long id) {
        storyService.deleteStory(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<StoryDto> getRandomStory(String cityName) {
        return ResponseEntity.ok(storyService.getRandomStoryForCity(cityName));
    }

}
