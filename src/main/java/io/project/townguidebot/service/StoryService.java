package io.project.townguidebot.service;

import io.project.townguidebot.dto.StoryDto;

import java.util.List;

public interface StoryService {
    StoryDto getRandomStoryForCity(String cityName);
    List<StoryDto> findAllStories();
    StoryDto findStoryById(Long id);
    StoryDto createStory(StoryDto storyDto);
    StoryDto updateStory(long id, StoryDto storyDto);
    void deleteStory(long id);
}
