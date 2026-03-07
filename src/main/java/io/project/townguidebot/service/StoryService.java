package io.project.townguidebot.service;

import io.project.townguidebot.dto.StoryDto;

import io.project.townguidebot.dto.request.StoryRq;
import java.util.List;

public interface StoryService {
    StoryDto getRandomStoryForCity(String cityName);
    List<StoryDto> findAllStories();
    StoryDto findStoryById(Long id);
    StoryDto createStory(StoryRq req);
    StoryDto updateStory(long id, StoryDto storyDto);
    void deleteStory(long id);
}
