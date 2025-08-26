package io.project.townguidebot.service.impl;

import io.project.townguidebot.exception.PhotoNotFoundException;
import io.project.townguidebot.model.Photo;
import io.project.townguidebot.repository.PhotoRepository;
import io.project.townguidebot.service.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional(readOnly = true)
    public String getPhotoUrl(Long photoId) {
        log.info("Get url for photo: {}", photoId);
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new PhotoNotFoundException("Photo not found: " + photoId));
        return photo.getUrl();
    }
}
