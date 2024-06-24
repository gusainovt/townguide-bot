package io.project.townguidebot.service.impl;

import io.project.townguidebot.exception.PhotoNotFoundException;
import io.project.townguidebot.mapper.PhotoMapper;
import io.project.townguidebot.mapper.PlaceMapper;
import io.project.townguidebot.model.Photo;
import io.project.townguidebot.model.Place;
import io.project.townguidebot.model.dto.ImagePreviewDto;
import io.project.townguidebot.repository.PhotoRepository;
import io.project.townguidebot.service.PhotoService;
import io.project.townguidebot.service.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static io.project.townguidebot.service.constants.ErrorText.ERROR_PHOTO_NOT_FOUND;
import static io.project.townguidebot.service.constants.ErrorText.ERROR_TEXT;
import static io.project.townguidebot.service.constants.LogText.METHOD_CALLED;
import static io.project.townguidebot.service.constants.LogText.WITH_ID;
import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;
    private final PlaceService placeService;
    private final PlaceMapper placeMapper;
    private final PhotoMapper photoMapper;

    @Value("${path.photo.folder}")
    private String photoDir;

    /**
     * Загружает фотографию в БД и в память
     * @param placeId ID места {@link io.project.townguidebot.model.Place}
     * @throws IOException ошибка ввода/вывода
     */
    @Override
    public void uploadPhoto(Long placeId, MultipartFile file) throws IOException {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName() + WITH_ID + placeId);
        Place place = placeMapper.toPlace(placeService.findPlaceById(placeId));
        place.setId(placeId);
        Path filePath = Path.of(photoDir, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) +
                "." + getExtension(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = file.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }
        Photo photo = new Photo();
        photo.setPlace(place);
        photo.setFilePath(filePath.toString());
        photo.setFileSize(file.getSize());
        photo.setMediaType(file.getContentType());
        photo.setData(file.getBytes());
        photoRepository.save(photo);
        Path finalFilePath = Path.of(photoDir, placeId + "_" + photo.getId() + "." + getExtension(file.getOriginalFilename()));
        Files.move(filePath, finalFilePath);
        photo.setFilePath(finalFilePath.toString());
        photoRepository.save(photo);
    }

    /**
     * Возращает превью фотографии по индефикатору
     * @param id ID фотографии
     * @return {@link ImagePreviewDto}
     */
    @Override
    public ImagePreviewDto generateImagePreview(Long id) {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
        Photo photo = photoRepository.findById(id).orElseThrow(()->{
            PhotoNotFoundException placeEx = new PhotoNotFoundException(String.format(ERROR_PHOTO_NOT_FOUND, id));
            log.error(ERROR_TEXT + placeEx.getMessage());
            return placeEx;
                });
        return photoMapper.photoToImagePreviewDto(photo);
    }

    @Override
    public String getPhotoPathById(Long id) {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
        Photo photo = photoRepository.findById(id).orElseThrow();
        String path = photo.getFilePath()
                .replaceFirst("\\\\Users\\\\Huawei\\\\IdeaProjects\\\\Telegram_bots\\\\BorovskBot\\\\src\\\\main\\\\resources\\\\", "");
        return path;
    }

    /**
     * Возращает расширение
     * @param fileName Название файла
     * @return Путь в виде строки
     */
    private String getExtension(String fileName) {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}
