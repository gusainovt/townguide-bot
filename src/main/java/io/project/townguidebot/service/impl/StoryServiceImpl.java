package io.project.townguidebot.service.impl;

import io.project.townguidebot.exception.StoryNotFoundException;
import io.project.townguidebot.mapper.StoryMapper;
import io.project.townguidebot.model.Story;
import io.project.townguidebot.model.dto.StoryDto;
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
    public StoryDto getRandomStoryForCity(Long chatId) {
        String cityName = cityService.getSelectedCityForChat(chatId);
        log.info("Find random story city: {} for chat: {}", cityName, chatId);
        return storyMapper.toStoryDto(storyRepository.findRandomStory().orElseThrow(() -> {
            log.error("Random story not found");
            return  new StoryNotFoundException("Random story not found");
        }));
    }

    /**
     * Находит все истории в БД
     * @return список историй {@link StoryDto}
     */
    @Override
    public List<StoryDto> findAllStories() {
        log.info("Find all stories...");
        return storyMapper.toListStoriesDto(storyRepository.findAll());
    }

    /**
     * Находит историю по ID
     * @param id идентификатор истории
     * @return найденная история {@link StoryDto}
     */
    @Override
    public StoryDto findStoryById(Long id) {
        log.info("Find story by id: {}", id);
        return storyMapper.toStoryDto(storyRepository.findById(id).orElseThrow(() -> {
            log.error("Story with id: {} not found", id);
            return new StoryNotFoundException(String.format("Story with id: %s not found", id));
        }));
    }

    /**
     * Создает новую историю
     * @param storyDto объект истории {@link StoryDto}
     * @return созданная история {@link StoryDto}
     */
    @Override
    public StoryDto createStory(StoryDto storyDto) {
        log.info("Create new story...");
        Story story = storyMapper.toStory(storyDto);
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
        log.info("Update story with id: {}", id);
        if (storyRepository.existsById(id)) {
            Story story = storyMapper.toStory(storyDto);
            story.setId(id);
            return storyMapper.toStoryDto(storyRepository.save(story));
        } else {
            log.error("Story with id: {} not found", id);
            throw new StoryNotFoundException(String.format("Story with id: %s not found", id));
        }
    }

    /**
     * Удаляет историю по ID
     * @param id идентификатор истории
     */
    @Override
    public void deleteStory(long id) {
        log.info("Delete story with id: {}", id);
        if (storyRepository.existsById(id)) {
            storyRepository.deleteById(id);
        } else {
            log.error("Story with id: {} not found", id);
            throw new StoryNotFoundException(String.format("Story with id: %s not found", id));
        }
    }

}
