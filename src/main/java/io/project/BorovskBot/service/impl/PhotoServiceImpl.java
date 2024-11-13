package io.project.BorovskBot.service.impl;

import io.project.BorovskBot.model.Photo;
import io.project.BorovskBot.model.Place;
import io.project.BorovskBot.repository.PhotoRepository;
import io.project.BorovskBot.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.apache.commons.io.FilenameUtils.getExtension;

@Service
@Slf4j
@RequiredArgsConstructor
public class PhotoServiceImpl implements io.project.BorovskBot.service.PhotoService {

    private final PhotoRepository photoRepository;
    private final PlaceRepository placeRepository;

    @Override
    public void uploadPhoto(Long placeId) throws IOException {
        Place place = placeRepository.getReferenceById(placeId);
        Path filePath = Path.of(avatarsDir, studentId + "." + getExtension(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = file.getInputStream();
                OutputStream os = Files.newOutputStream();
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }

        File file = new ClassPathResource("media/place_photo/1.jpg").getFile();

        Photo photo = new Photo();
        photo.setPlace(place);
        photo.setFilePath(filePath.toString());
        photo.setFileSize(file.getUsableSpace());
        photo.setData(Files.readAllBytes(Paths.get(file.getPath())));
        place.getPhoto().add(photo);
        photoRepository.save(photo);

    }
}
