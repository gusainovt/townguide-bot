package io.project.BorovskBot.service;

import io.project.BorovskBot.model.dto.ImagePreviewDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PhotoService {
    void uploadPhoto(Long placeId, MultipartFile file) throws IOException;
    ImagePreviewDto generateImagePreview(Long id);
}
