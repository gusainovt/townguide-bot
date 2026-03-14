package io.project.townguidebot.service;

import io.project.townguidebot.dto.UploadPhotoResult;
import io.project.townguidebot.dto.UploadedFile;

import java.io.IOException;

public interface CloudinaryService {

    /**
     * Загружает фото в Cloudinary
     * @param placeId id места
     * @param file фото
     * @return сущность Photo
     */
    UploadPhotoResult uploadPhoto(UploadedFile file, Long placeId) throws IOException;

    /**
     * Удаляет фото из Cloudinary
     * @param publicId публичный айди
     */
    void deletePhoto(String publicId) throws IOException;
}
