package io.project.townguidebot.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.project.townguidebot.dto.UploadPhotoResult;
import io.project.townguidebot.dto.UploadedFile;
import io.project.townguidebot.exception.PhotoUploadException;
import io.project.townguidebot.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;


    @Override
    public UploadPhotoResult uploadPhoto(UploadedFile file, Long placeId) {
        log.info("Upload photo for place {}", placeId);
        try {
            Map uploadResult = cloudinary.uploader().upload(file.bytes(),
                    ObjectUtils.asMap(
                            "folder", "places/" + placeId,
                            "resource_type", "image"
                    ));

            String url = uploadResult.get("secure_url").toString();
            String publicId = uploadResult.get("public_id").toString();
            return UploadPhotoResult.builder()
                .url(url)
                .fileSize(file.bytes() == null ? 0L : (long) file.bytes().length)
                .mediaType(file.contentType())
                .publicId(publicId)
                .build();
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
