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
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(readOnly = true)
    @Override
    public PlaceDto findPlaceById(Long id) {
        log.info("Find place by id: {}", id);
        return placeMapper.toPlaceDto(placeRepository.findById(id).orElseThrow(() -> {
            log.error("Place with id: {} not found", id);
            return new PlaceNotFoundException();
        }));
    }

    /**
     * Создает новое место
     * @param placeDto объект места {@link PlaceDto}
     * @return созданное место {@link PlaceDto}
     */
    @Transactional
    @Override
    public PlaceDto createPlace(PlaceDto placeDto) {
        log.info("Create new palace...");
        Place place = placeMapper.toPlace(placeDto);
        return placeMapper.toPlaceDto(placeRepository.save(place));
    }

    /**
     * Обновляет существующее место
     * @param id идентификатор места
     * @param placeDto объект места {@link PlaceDto}
     * @return обновленное место {@link PlaceDto}
     */
    @Transactional
    @Override
    public PlaceDto updatePlace(Long id, PlaceDto placeDto) {
        log.info("Updating place by id: {}", id);
        if (placeRepository.existsById(id)) {
            Place place = placeMapper.toPlace(placeDto);
            place.setId(id);
            return placeMapper.toPlaceDto(placeRepository.save(place));
        } else {
            log.error("Place with id: {} not found", id);
            throw new PlaceNotFoundException();
        }
    }

    /**
     * Удаляет место по ID
     * @param id идентификатор места
     */
    @Transactional
    @Override
    public void deletePlace(Long id) {
        log.info("Delete place with id: {}", id);
        if (placeRepository.existsById(id)) {
            placeRepository.deleteById(id);
        } else {
            log.error("Place with id: {} not found", id);
            throw new PlaceNotFoundException();
        }
    }

    /**
     * Находит место в БД по рандомному ID
     * @return История {@link Place}
     */
    @Transactional(readOnly = true)
    @Override
    public PlaceDto getRandomStory() {
        log.info("Get random place");
        return placeMapper.toPlaceDto(placeRepository.findRandomPlace().orElseThrow(() -> {
                    log.error("Random place not found");
                    return new PlaceNotFoundException();
                }));
    }

}
