package io.project.BorovskBot.controller;

import io.project.BorovskBot.model.dto.ImagePreviewDto;
import io.project.BorovskBot.service.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/photos")
public class PhotoController {
    private final PhotoService photoService;
    @GetMapping(value = "/{id}")
    public ResponseEntity<byte[]> getPhotoPreview(@PathVariable Long id) {
        ImagePreviewDto preview = photoService.generateImagePreview(id);
        return ResponseEntity.status(HttpStatus.OK).headers(preview.getHeaders()).body(preview.getData());
    }

    @PostMapping(value = "/{placeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> savePhoto(@PathVariable Long placeId, @RequestBody MultipartFile multipartFile) throws IOException {
        photoService.uploadPhoto(placeId,multipartFile);
        return ResponseEntity.ok().build();
    }

}
