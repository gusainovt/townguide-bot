package io.project.townguidebot.service;

import io.project.townguidebot.model.Photo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {

    /**
     * Загружает фото в Cloudinary
     * @param placeId id места
     * @param file фото
     * @return сущность Photo
     */
    Photo uploadPhoto (MultipartFile file, Long placeId) throws IOException;

    /**
     * Удаляет фото из Cloudinary
     * @param publicId публичный айди
     */
    void deletePhoto(String publicId) throws IOException;
}
