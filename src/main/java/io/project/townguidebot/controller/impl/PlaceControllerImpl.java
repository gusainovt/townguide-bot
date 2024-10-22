package io.project.townguidebot.controller.impl;

import io.project.townguidebot.controller.PlaceController;
import io.project.townguidebot.dto.PlaceDto;
import io.project.townguidebot.service.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PlaceControllerImpl implements PlaceController {

    private final PlaceService placeService;

    @Override
    public ResponseEntity<PlaceDto> getPlaceById(Long id) {
        PlaceDto placeDto = placeService.findPlaceById(id);
        return ResponseEntity.ok(placeDto);
    }

    @Override
    public ResponseEntity<PlaceDto> createPlace(PlaceDto placeDto) {
        PlaceDto createdPlace = placeService.createPlace(placeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlace);
    }

    @Override
    public ResponseEntity<PlaceDto> updatePlace(Long id, PlaceDto placeDto) {
        PlaceDto updatedPlace = placeService.updatePlace(id, placeDto);
        return ResponseEntity.ok(updatedPlace);
    }

    @Override
    public ResponseEntity<Void> deletePlace(Long id) {
        placeService.deletePlace(id);
        return ResponseEntity.noContent().build();
    }
}
