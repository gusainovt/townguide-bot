package io.project.townguidebot.service.impl;

import io.project.townguidebot.exception.StoryNotFoundException;
import io.project.townguidebot.mapper.StoryMapper;
import io.project.townguidebot.model.Story;
import io.project.townguidebot.model.dto.StoryDto;
import io.project.townguidebot.repository.StoryRepository;
import io.project.townguidebot.service.StoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static io.project.townguidebot.service.constants.ErrorText.ERROR_STORY_NOT_FOUND;
import static io.project.townguidebot.service.constants.ErrorText.ERROR_TEXT;
import static io.project.townguidebot.service.constants.LogText.METHOD_CALLED;
import static io.project.townguidebot.service.constants.LogText.WITH_ID;

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
        return storyMapper.toStoryDto(storyRepository.findRandomStory().orElseThrow(() -> {
            StoryNotFoundException storyEx = new StoryNotFoundException(String.format(ERROR_STORY_NOT_FOUND, "random"));
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
