package io.project.townguidebot.service;

import io.project.townguidebot.model.Place;
import io.project.townguidebot.model.dto.PlaceDto;

import java.util.List;

public interface PlaceService {
    PlaceDto findPlaceById(Long id);
    PlaceDto createPlace(PlaceDto placeDto);
    PlaceDto updatePlace(Long id, PlaceDto placeDto);
    void deletePlace(Long id);
    PlaceDto getRandomPlaceByCity(String cityName);
    Long getSelectedPlaceForChat(Long chatId);
    void unselectedPlaceForChat(Long chatId);
    Long selectedPlace(String callbackData, Long chatId);
    List<Place> getPlacesByNameCity(String cityName);
}
