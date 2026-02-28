package io.project.townguidebot.service.impl;

import io.project.townguidebot.dto.StoryDto;
import io.project.townguidebot.exception.StoryNotFoundException;
import io.project.townguidebot.mapper.StoryMapper;
import io.project.townguidebot.model.Story;
import io.project.townguidebot.repository.StoryRepository;
import io.project.townguidebot.service.CityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoryServiceImplTest {

    @Mock
    private StoryRepository storyRepository;

    @Mock
    private StoryMapper storyMapper;

    @Mock
    private CityService cityService;

    @InjectMocks
    private StoryServiceImpl storyService;

    @Test
    void getRandomStoryForCity_WhenFound_ShouldReturnDto() {
        String city = "moscow";
        Story story = new Story();
        StoryDto dto = new StoryDto();
        when(storyRepository.findRandomStoryByNameCity(city)).thenReturn(Optional.of(story));
        when(storyMapper.toStoryDto(story)).thenReturn(dto);

        StoryDto result = storyService.getRandomStoryForCity(city);

        assertSame(dto, result);
        verify(storyRepository).findRandomStoryByNameCity(city);
        verify(storyMapper).toStoryDto(story);
    }

    @Test
    void getRandomStoryForCity_WhenNotFound_ShouldThrowException() {
        when(storyRepository.findRandomStoryByNameCity("moscow")).thenReturn(Optional.empty());

        StoryNotFoundException ex = assertThrows(StoryNotFoundException.class,
                () -> storyService.getRandomStoryForCity("moscow"));

        assertEquals("Random story not found", ex.getMessage());
        verify(storyRepository).findRandomStoryByNameCity("moscow");
    }

    @Test
    void findAllStories_ShouldReturnMappedDtos() {
        List<Story> stories = List.of(new Story(), new Story());
        List<StoryDto> dtos = List.of(new StoryDto(), new StoryDto());
        when(storyRepository.findAll()).thenReturn(stories);
        when(storyMapper.toListStoriesDto(stories)).thenReturn(dtos);

        List<StoryDto> result = storyService.findAllStories();

        assertSame(dtos, result);
        verify(storyRepository).findAll();
        verify(storyMapper).toListStoriesDto(stories);
    }

    @Test
    void findStoryById_WhenFound_ShouldReturnDto() {
        Long id = 1L;
        Story story = new Story();
        StoryDto dto = new StoryDto();
        when(storyRepository.findById(id)).thenReturn(Optional.of(story));
        when(storyMapper.toStoryDto(story)).thenReturn(dto);

        StoryDto result = storyService.findStoryById(id);

        assertSame(dto, result);
        verify(storyRepository).findById(id);
        verify(storyMapper).toStoryDto(story);
    }

    @Test
    void findStoryById_WhenNotFound_ShouldThrowException() {
        Long id = 1L;
        when(storyRepository.findById(id)).thenReturn(Optional.empty());

        StoryNotFoundException ex = assertThrows(StoryNotFoundException.class,
                () -> storyService.findStoryById(id));

        assertEquals("Story with id: 1 not found", ex.getMessage());
        verify(storyRepository).findById(id);
    }

    @Test
    void createStory_ShouldMapSaveAndReturnDto() {
        StoryDto input = new StoryDto();
        Story mapped = new Story();
        Story saved = new Story();
        StoryDto output = new StoryDto();

        when(storyMapper.toStory(input)).thenReturn(mapped);
        when(storyRepository.save(mapped)).thenReturn(saved);
        when(storyMapper.toStoryDto(saved)).thenReturn(output);

        StoryDto result = storyService.createStory(input);

        assertSame(output, result);
        verify(storyMapper).toStory(input);
        verify(storyRepository).save(mapped);
        verify(storyMapper).toStoryDto(saved);
    }

    @Test
    void updateStory_WhenExists_ShouldSaveWithIdAndReturnDto() {
        long id = 7L;
        StoryDto input = new StoryDto();
        Story mapped = new Story();
        Story saved = new Story();
        StoryDto output = new StoryDto();

        when(storyRepository.existsById(id)).thenReturn(true);
        when(storyMapper.toStory(input)).thenReturn(mapped);
        when(storyRepository.save(any(Story.class))).thenReturn(saved);
        when(storyMapper.toStoryDto(saved)).thenReturn(output);

        StoryDto result = storyService.updateStory(id, input);

        assertSame(output, result);
        ArgumentCaptor<Story> captor = ArgumentCaptor.forClass(Story.class);
        verify(storyRepository).save(captor.capture());
        assertEquals(id, captor.getValue().getId());
        verify(storyRepository).existsById(id);
        verify(storyMapper).toStory(input);
        verify(storyMapper).toStoryDto(saved);
    }

    @Test
    void updateStory_WhenNotFound_ShouldThrowException() {
        long id = 7L;
        when(storyRepository.existsById(id)).thenReturn(false);

        StoryNotFoundException ex = assertThrows(StoryNotFoundException.class,
                () -> storyService.updateStory(id, new StoryDto()));

        assertEquals("Story with id: 7 not found", ex.getMessage());
        verify(storyRepository).existsById(id);
        verify(storyRepository, never()).save(any());
    }

    @Test
    void deleteStory_WhenExists_ShouldDelete() {
        long id = 2L;
        when(storyRepository.existsById(id)).thenReturn(true);

        storyService.deleteStory(id);

        verify(storyRepository).existsById(id);
        verify(storyRepository).deleteById(id);
    }

    @Test
    void deleteStory_WhenNotFound_ShouldThrowException() {
        long id = 2L;
        when(storyRepository.existsById(id)).thenReturn(false);

        StoryNotFoundException ex = assertThrows(StoryNotFoundException.class,
                () -> storyService.deleteStory(id));

        assertEquals("Story with id: 2 not found", ex.getMessage());
        verify(storyRepository).existsById(id);
        verify(storyRepository, never()).deleteById(any());
    }
}

