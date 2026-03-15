package io.project.townguidebot.service.impl;

import io.project.townguidebot.dto.request.StoryRq;
import io.project.townguidebot.exception.StoryNotFoundException;
import io.project.townguidebot.mapper.StoryMapper;
import io.project.townguidebot.model.Story;
import io.project.townguidebot.dto.StoryDto;
import io.project.townguidebot.repository.StoryRepository;
import io.project.townguidebot.service.CityService;
import io.project.townguidebot.service.StoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StoryServiceImpl implements StoryService {

    private final StoryRepository storyRepository;
    private final StoryMapper storyMapper;
    private final CityService cityService;

    /**
     * Находит историю в БД по рандомному ID
     * @return История {@link Story}
     */
    @Override
    public StoryDto getRandomStoryForCity(String cityName) {
        log.debug("Find random story for city: {}", cityName);
        Story story = storyRepository.findRandomStoryByNameCity(cityName).orElseThrow(() -> {
            log.warn("Random story for city: {} not found", cityName);
            return  new StoryNotFoundException("Random story not found");
        });
        return storyMapper.toStoryDto(story);
    }

    /**
     * Находит все истории в БД
     * @return список историй {@link StoryDto}
     */
    @Override
    public List<StoryDto> findAllStories() {
        log.debug("Find all stories");
        return storyMapper.toListStoriesDto(storyRepository.findAll());
    }

    /**
     * Находит историю по ID
     * @param id идентификатор истории
     * @return найденная история {@link StoryDto}
     */
    @Override
    public StoryDto findStoryById(Long id) {
        log.debug("Find story by id: {}", id);
        return storyMapper.toStoryDto(storyRepository.findById(id).orElseThrow(() -> {
            log.warn("Story with id: {} not found", id);
            return new StoryNotFoundException(String.format("Story with id: %s not found", id));
        }));
    }

    /**
     * Создает новую историю
     *
     * @param req {@link StoryRq}
     * @return созданная история {@link StoryDto}
     */
    @Override
    public StoryDto createStory(StoryRq req) {
        log.debug("Create story");
        Story story = storyMapper.toStory(req);
        story.setCity(cityService.findCityById(req.getCityId()));
        return storyMapper.toStoryDto(storyRepository.save(story));
    }

    /**
     * Обновляет существующую историю
     * @param id идентификатор истории
     * @param storyDto объект истории {@link StoryDto}
     * @return обновленная история {@link StoryDto}
     */
    @Override
    public StoryDto updateStory(long id, StoryDto storyDto) {
        log.debug("Update story with id: {}", id);
        Story existing = storyRepository.findById(id).orElseThrow(() -> {
            log.warn("Story with id: {} not found", id);
            return new StoryNotFoundException(String.format("Story with id: %s not found", id));
        });

        existing.setBody(storyDto.getBody());
        existing.setCity(cityService.findCityById(storyDto.getCityId()));

        return storyMapper.toStoryDto(storyRepository.save(existing));
    }

    /**
     * Удаляет историю по ID
     * @param id идентификатор истории
     */
    @Override
    public void deleteStory(long id) {
        log.debug("Delete story with id: {}", id);
        if (storyRepository.existsById(id)) {
            storyRepository.deleteById(id);
        } else {
            log.warn("Story with id: {} not found", id);
            throw new StoryNotFoundException(String.format("Story with id: %s not found", id));
        }
    }

}
