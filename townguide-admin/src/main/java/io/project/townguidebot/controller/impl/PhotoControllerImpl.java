package io.project.townguidebot.controller.impl;

import io.project.townguidebot.controller.PhotoController;
import io.project.townguidebot.dto.UploadedFile;
import io.project.townguidebot.service.CloudinaryService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PhotoControllerImpl implements PhotoController {

    private final CloudinaryService cloudinaryService;

    @Override
    public ResponseEntity<String> savePhoto(Long placeId, @RequestPart("file") MultipartFile multipartFile) throws IOException {
        UploadedFile uploadedFile = UploadedFile.builder()
                .bytes(multipartFile.getBytes())
                .originalFilename(multipartFile.getOriginalFilename())
                .contentType(multipartFile.getContentType())
                .build();
        cloudinaryService.uploadPhoto(uploadedFile, placeId);
        return ResponseEntity.ok().build();
    }

}
