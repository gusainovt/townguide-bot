package io.project.BorovskBot.service;

import io.project.BorovskBot.model.dto.PlaceDto;

public interface PlaceService {

    PlaceDto findPlaceById(Long id);
    PlaceDto createPlace(PlaceDto placeDto);
    PlaceDto updatePlace(Long id, PlaceDto placeDto);
    void deletePlace(Long id);
    PlaceDto getRandomStory();
}
