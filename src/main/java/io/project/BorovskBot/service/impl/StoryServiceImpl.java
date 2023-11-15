package io.project.BorovskBot.service.impl;

import io.project.BorovskBot.exception.StoryNotFoundException;
import io.project.BorovskBot.mapper.StoryMapper;
import io.project.BorovskBot.model.Story;
import io.project.BorovskBot.model.dto.StoryDto;
import io.project.BorovskBot.repository.StoryRepository;
import io.project.BorovskBot.service.StoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

import static io.project.BorovskBot.service.constants.ErrorText.*;
import static io.project.BorovskBot.service.constants.LogText.*;
import static io.project.BorovskBot.service.constants.TelegramText.MAX_STORY_ID_MINUS_ONE;

@Service
@Slf4j
@RequiredArgsConstructor
public class StoryServiceImpl implements StoryService {

    private final StoryRepository storyRepository;
    private final StoryMapper storyMapper;

    /**
     * Находит историю в БД по рандомному ID
     * @return История {@link Story}
     */
    @Override
    public StoryDto getRandomStory() {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
        var r = new Random();
        var randomId = (long)(r.nextInt(MAX_STORY_ID_MINUS_ONE) + 1);
        return storyMapper.toStoryDto(storyRepository.findById(randomId).orElseThrow(()->{
            StoryNotFoundException storyEx = new StoryNotFoundException(String.format(ERROR_STORY_NOT_FOUND, randomId));
            log.error(ERROR_TEXT + storyEx.getMessage());
            return storyEx;
        }));
    }

    /**
     * Находит все истории в БД
     * @return список историй {@link StoryDto}
     */
    @Override
    public List<StoryDto> findAllStories() {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
        return storyMapper.toListStoriesDto(storyRepository.findAll());
    }

    /**
     * Находит историю по ID
     * @param id идентификатор истории
     * @return найденная история {@link StoryDto}
     */
    @Override
    public StoryDto findStoryById(Long id) {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName() + WITH_ID + id);
        return storyMapper.toStoryDto(storyRepository.findById(id).orElseThrow(() -> {
            StoryNotFoundException storyEx = new StoryNotFoundException(String.format(ERROR_STORY_NOT_FOUND, id));
            log.error(ERROR_TEXT + storyEx.getMessage());
            return storyEx;
        }));
    }

    /**
     * Создает новую историю
     * @param storyDto объект истории {@link StoryDto}
     * @return созданная история {@link StoryDto}
     */
    @Override
    public StoryDto createStory(StoryDto storyDto) {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
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
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName() + WITH_ID + id);
        if (storyRepository.existsById(id)) {
            Story story = storyMapper.toStory(storyDto);
            story.setId(id);
            return storyMapper.toStoryDto(storyRepository.save(story));
        } else {
            String message = String.format(ERROR_STORY_NOT_FOUND, id);
            log.error(ERROR_TEXT + message);
            throw new StoryNotFoundException(message);
        }
    }

    /**
     * Удаляет историю по ID
     * @param id идентификатор истории
     */
    @Override
    public void deleteStory(long id) {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName() + WITH_ID + id);
        if (storyRepository.existsById(id)) {
            storyRepository.deleteById(id);
        } else {
            String message = String.format(ERROR_STORY_NOT_FOUND, id);
            log.error(ERROR_TEXT + message);
            throw new StoryNotFoundException(message);
        }
    }

}
