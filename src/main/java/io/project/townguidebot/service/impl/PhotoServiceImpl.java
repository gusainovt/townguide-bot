package io.project.townguidebot.service.impl;

import io.project.townguidebot.exception.PhotoNotFoundException;
import io.project.townguidebot.model.Photo;
import io.project.townguidebot.repository.PhotoRepository;
import io.project.townguidebot.service.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;

    @Override
    @Transactional
    public Photo savePhoto(Photo photo) {
        log.info("Created new photo...");
        return photoRepository.save(photo);
    }

    /**
     * Получить все фотографии места по айди места
     * @param placeId айди места
     * @return список фотографий
     */
    @Override
    public List<Photo> getAllPhotoByPlace(Long placeId) {
        log.info("Get all photos by place id: {}", placeId);
        return photoRepository.findPhotosByPlaceId(placeId).orElseThrow(() -> {
            log.error("Photos not found by place id: {}", placeId);
            return new PhotoNotFoundException("Photos not found by place id: " + placeId);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public String getPhotoUrl(Long photoId) {
        log.info("Get url for photo: {}", photoId);
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new PhotoNotFoundException("Photo not found: " + photoId));
        return photo.getUrl();
    }
}
