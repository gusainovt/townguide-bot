package io.project.townguidebot.service;

import io.project.townguidebot.dto.UploadPhotoResult;
import io.project.townguidebot.model.Photo;

import java.util.List;

public interface PhotoService {

    /**
     * Получает ссылку на фото по id
     * @param photoId id фото
     * @return url картинки
     */
    String getPhotoUrl(Long photoId);

    /**
     * Сохраняет ссылку на фото в БД
     *
     * @param placeId Идентификатор места
     * @param uploadPhoto Данные от Cloudinary
     * @return сущность Photo
     */
    Photo savePhoto(Long placeId, UploadPhotoResult uploadPhoto);

    List<Photo> getAllPhotoByPlace(Long placeId);
}
