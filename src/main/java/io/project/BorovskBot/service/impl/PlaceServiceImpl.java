package io.project.BorovskBot.service.impl;

import io.project.BorovskBot.model.Place;
import io.project.BorovskBot.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static io.project.BorovskBot.service.constants.LogText.METHOD_CALLED;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlaceServiceImpl implements io.project.BorovskBot.service.PlaceService {

    private final PlaceRepository placeRepository;

    /**
     * Добавляет новое место в БД
     * @param place объект места {@link Place}
     * @return объект места {@link Place}
     */
    @Override
    public Place addPlace(Place place) {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
        return placeRepository.save(place);
    }

}
