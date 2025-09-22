package io.project.townguidebot.service;

import io.project.townguidebot.model.dto.PlaceDto;

public interface PlaceService {

    PlaceDto findPlaceById(Long id);
    PlaceDto createPlace(PlaceDto placeDto);
    PlaceDto updatePlace(Long id, PlaceDto placeDto);
    void deletePlace(Long id);
    PlaceDto getRandomStoryByChatId(Long chatId) ;
}
