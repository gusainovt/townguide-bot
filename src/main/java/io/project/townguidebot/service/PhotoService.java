package io.project.townguidebot.service;

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
     * @param photo сущность Photo
     * @return сущность Photo
     */
    Photo savePhoto(Photo photo);

    List<Photo> getAllPhotoByPlace(Long placeId);
}
