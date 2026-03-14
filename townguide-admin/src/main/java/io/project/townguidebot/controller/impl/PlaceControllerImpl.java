package io.project.townguidebot.controller.impl;

import io.project.townguidebot.controller.PlaceController;
import io.project.townguidebot.dto.PlaceDto;
import io.project.townguidebot.dto.UploadedFile;
import io.project.townguidebot.dto.request.PlaceCreateRq;
import io.project.townguidebot.dto.response.PlaceCreateRs;
import io.project.townguidebot.service.PlaceService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<PlaceCreateRs> create(PlaceCreateRq req, MultipartFile file)
        throws IOException {
        UploadedFile uploadedFile = UploadedFile.builder()
                .bytes(file.getBytes())
                .originalFilename(file.getOriginalFilename())
                .contentType(file.getContentType())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(placeService.create(req, uploadedFile));
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
