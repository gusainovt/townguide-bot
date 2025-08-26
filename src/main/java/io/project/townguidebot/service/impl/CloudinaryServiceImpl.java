package io.project.townguidebot.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.project.townguidebot.exception.PhotoUploadException;
import io.project.townguidebot.mapper.PlaceMapper;
import io.project.townguidebot.model.Photo;
import io.project.townguidebot.model.Place;
import io.project.townguidebot.service.CloudinaryService;
import io.project.townguidebot.service.PhotoService;
import io.project.townguidebot.service.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    private final PhotoService photoService;
    private final PlaceService placeService;
    private final PlaceMapper placeMapper;
    private final Cloudinary cloudinary;

    @Override
    public Photo uploadPhoto(MultipartFile file, Long placeId) throws IOException {
        log.info("Upload photo for place {}", placeId);
        try {
        Place place = placeMapper.toPlace(placeService.findPlaceById(placeId));
        place.setId(placeId);

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "folder", "places/" + placeId,
                        "resource_type", "image"
                ));

        String url = uploadResult.get("secure_url").toString();
        String publicId = uploadResult.get("public_id").toString();

        return photoService.savePhoto(Photo.builder()
                .place(place).url(url)
                .fileSize(file.getSize())
                .mediaType(file.getContentType())
                .publicId(publicId)
                .build());
        } catch (IOException e) {
            log.error("Failed to upload photo to Cloudinary for place {}", placeId, e);
            throw new PhotoUploadException("Failed to upload photo to Cloudinary");
        }
    }

    @Override
    public void deletePhoto(String publicId) throws IOException {
        log.info("Deleting photo from Cloudinary: {}", publicId);
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            log.error("Failed to delete photo from Cloudinary", e);
            throw new PhotoUploadException("Failed to delete photo from Cloudinary");
        }
    }
}
