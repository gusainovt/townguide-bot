package io.project.townguidebot.controller;

import io.project.townguidebot.model.dto.ImagePreviewDto;
import io.project.townguidebot.service.PhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Фотографии")
public class PhotoController {

    private final PhotoService photoService;

    @Operation(summary = "Получить превью фотографии", description = "Возвращает превью фотографии по указанному идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Превью фотографии успешно возвращено"),
            @ApiResponse(responseCode = "404", description = "Фотография не найдена"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<byte[]> getPhotoPreview(@PathVariable Long id) {
        ImagePreviewDto preview = photoService.generateImagePreview(id);
        return ResponseEntity.status(HttpStatus.OK).headers(preview.getHeaders()).body(preview.getData());
    }

    @Operation(summary = "Сохранить фотографию", description = "Сохраняет фотографию для указанного места.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Фотография успешно сохранена"),
            @ApiResponse(responseCode = "400", description = "Ошибка в данных фотографии"),
            @ApiResponse(responseCode = "404", description = "Место не найдено"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @PostMapping(value = "/{placeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> savePhoto(@PathVariable Long placeId, @RequestBody MultipartFile multipartFile) throws IOException {
        photoService.uploadPhoto(placeId,multipartFile);
        return ResponseEntity.ok().build();
    }

}
