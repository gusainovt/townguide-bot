package io.project.townguidebot.service;

import io.project.townguidebot.dto.request.PlaceCreateRq;
import io.project.townguidebot.dto.response.PlaceCreateRs;
import io.project.townguidebot.model.Place;
import io.project.townguidebot.dto.PlaceDto;

import java.io.IOException;
import java.util.List;
import io.project.townguidebot.dto.UploadedFile;

public interface PlaceService {
    PlaceDto findPlaceById(Long id);
    PlaceDto updatePlace(Long id, PlaceDto placeDto);
    void deletePlace(Long id);
    PlaceDto getRandomPlaceByCity(String cityName);
    Long getSelectedPlaceForChat(Long chatId);
    void unselectedPlaceForChat(Long chatId);
    Long selectedPlace(String callbackData, Long chatId);
    List<Place> getPlacesByNameCity(String cityName);
    PlaceCreateRs create(PlaceCreateRq req, UploadedFile file) throws IOException;
}
