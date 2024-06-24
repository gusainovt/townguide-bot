package io.project.townguidebot.service;

import io.project.townguidebot.model.dto.StoryDto;

import java.util.List;

public interface StoryService {
    StoryDto getRandomStory();
    List<StoryDto> findAllStories();
    StoryDto findStoryById(Long id);
    StoryDto createStory(StoryDto storyDto);
    StoryDto updateStory(long id, StoryDto storyDto);
    void deleteStory(long id);
}
