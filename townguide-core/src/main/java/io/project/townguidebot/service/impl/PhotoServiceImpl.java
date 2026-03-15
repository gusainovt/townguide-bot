package io.project.townguidebot.service.impl;

import io.project.townguidebot.dto.UploadPhotoResult;
import io.project.townguidebot.exception.PhotoNotFoundException;
import io.project.townguidebot.mapper.PlaceMapper;
import io.project.townguidebot.model.Photo;
import io.project.townguidebot.model.Place;
import io.project.townguidebot.repository.PhotoRepository;
import io.project.townguidebot.repository.PlaceRepository;
import io.project.townguidebot.service.PhotoService;
import io.project.townguidebot.service.PlaceService;
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
    private final PlaceRepository placeRepository;

    @Override
    @Transactional
    public Photo savePhoto(Long placeId, UploadPhotoResult uploadPhoto) {
        log.debug("Create photo for place {}", placeId);
        Place place = placeRepository.getReferenceById(placeId);
        return photoRepository.save(Photo.builder()
            .place(place)
            .url(uploadPhoto.getUrl())
            .fileSize(uploadPhoto.getFileSize())
            .mediaType(uploadPhoto.getMediaType())
            .publicId(uploadPhoto.getPublicId())
            .build());
    }

    /**
     * Получить все фотографии места по айди места
     *
     * @param placeId айди места
     * @return список фотографий
     */
    @Override
    public List<Photo> getAllPhotoByPlace(Long placeId) {
        log.debug("Get all photos by place id: {}", placeId);
        return photoRepository.findPhotosByPlaceId(placeId).orElseThrow(() -> {
            log.warn("Photos not found by place id: {}", placeId);
            return new PhotoNotFoundException("Photos not found by place id: " + placeId);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public String getPhotoUrl(Long photoId) {
        log.debug("Get url for photo: {}", photoId);
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new PhotoNotFoundException("Photo not found: " + photoId));
        return photo.getUrl();
    }
}
