package io.project.townguidebot.mapper;

import io.project.townguidebot.dto.request.StoryRq;
import io.project.townguidebot.model.Story;
import io.project.townguidebot.dto.StoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "city", ignore = true)
    Story toStory(StoryRq req);

    @Mapping(source = "city.id", target = "cityId")
    StoryDto toStoryDto(Story story);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "city", ignore = true)
    Story toStory(StoryDto storyDto);

    List<StoryDto> toListStoriesDto(List<Story> story);
}
