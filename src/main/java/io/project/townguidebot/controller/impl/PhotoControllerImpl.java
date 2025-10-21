package io.project.townguidebot.controller.impl;

import io.project.townguidebot.controller.PhotoController;
import io.project.townguidebot.service.CloudinaryService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PhotoControllerImpl implements PhotoController {

    private final CloudinaryService cloudinaryService;

    @Override
    public ResponseEntity<String> savePhoto(Long placeId, MultipartFile multipartFile) throws IOException {
        cloudinaryService.uploadPhoto(multipartFile, placeId);
        return ResponseEntity.ok().build();
    }

}
