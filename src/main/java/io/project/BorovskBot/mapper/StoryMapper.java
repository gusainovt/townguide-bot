package io.project.BorovskBot.mapper;

import io.project.BorovskBot.model.Story;
import io.project.BorovskBot.model.dto.StoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StoryMapper {
    StoryDto toStoryDto(Story story);
    Story toStory(StoryDto storyDto);
    List<StoryDto> toListStoriesDto(List<Story> story);
}
