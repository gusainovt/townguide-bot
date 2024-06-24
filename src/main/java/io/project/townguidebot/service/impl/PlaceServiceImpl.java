package io.project.townguidebot.service.impl;

import io.project.townguidebot.exception.PlaceNotFoundException;
import io.project.townguidebot.mapper.PlaceMapper;
import io.project.townguidebot.model.Place;
import io.project.townguidebot.model.dto.PlaceDto;
import io.project.townguidebot.repository.PlaceRepository;
import io.project.townguidebot.service.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static io.project.townguidebot.service.constants.ErrorText.ERROR_PLACE_NOT_FOUND;
import static io.project.townguidebot.service.constants.ErrorText.ERROR_TEXT;
import static io.project.townguidebot.service.constants.LogText.METHOD_CALLED;
import static io.project.townguidebot.service.constants.LogText.WITH_ID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceMapper placeMapper;

    /**
     * Находит место по ID
     * @param id идентификатор места
     * @return найденное место {@link PlaceDto}
     */
    @Override
    public PlaceDto findPlaceById(Long id) {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName() + WITH_ID + id);
        return placeMapper.toPlaceDto(placeRepository.findById(id).orElseThrow(() -> {
            PlaceNotFoundException placeEx = new PlaceNotFoundException(String.format(ERROR_PLACE_NOT_FOUND, id));
            log.error(ERROR_TEXT + placeEx.getMessage());
            return placeEx;
        }));
    }

    /**
     * Создает новое место
     * @param placeDto объект места {@link PlaceDto}
     * @return созданное место {@link PlaceDto}
     */
    @Override
    public PlaceDto createPlace(PlaceDto placeDto) {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
        Place place = placeMapper.toPlace(placeDto);
        return placeMapper.toPlaceDto(placeRepository.save(place));
    }

    /**
     * Обновляет существующее место
     * @param id идентификатор места
     * @param placeDto объект места {@link PlaceDto}
     * @return обновленное место {@link PlaceDto}
     */
    @Override
    public PlaceDto updatePlace(Long id, PlaceDto placeDto) {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName() + WITH_ID + id);
        if (placeRepository.existsById(id)) {
            Place place = placeMapper.toPlace(placeDto);
            place.setId(id);
            return placeMapper.toPlaceDto(placeRepository.save(place));
        } else {
            String message = String.format(ERROR_PLACE_NOT_FOUND, id);
            log.error(ERROR_TEXT + message);
            throw new PlaceNotFoundException(message);
        }
    }

    /**
     * Удаляет место по ID
     * @param id идентификатор места
     */
    @Override
    public void deletePlace(Long id) {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName() + WITH_ID + id);
        if (placeRepository.existsById(id)) {
            placeRepository.deleteById(id);
        } else {
            String message = String.format(ERROR_PLACE_NOT_FOUND, id);
            log.error(ERROR_TEXT + message);
            throw new PlaceNotFoundException(message);
        }
    }

    /**
     * Находит место в БД по рандомному ID
     * @return История {@link Place}
     */
    @Override
    public PlaceDto getRandomStory() {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
        return placeMapper.toPlaceDto(placeRepository.findRandomPlace().orElseThrow(() -> {
                    PlaceNotFoundException placeEx = new PlaceNotFoundException(String.format(ERROR_PLACE_NOT_FOUND, "random"));
                    log.error(ERROR_TEXT + placeEx.getMessage());
                    return placeEx;
                }));
    }

}
