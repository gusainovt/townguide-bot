package io.project.townguidebot.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import io.project.townguidebot.dto.PlaceDto;
import io.project.townguidebot.exception.PhotoUploadException;
import io.project.townguidebot.mapper.PlaceMapper;
import io.project.townguidebot.model.Photo;
import io.project.townguidebot.model.Place;
import io.project.townguidebot.service.PhotoService;
import io.project.townguidebot.service.PlaceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CloudinaryServiceImplTest {

    @Mock
    private PhotoService photoService;

    @Mock
    private PlaceService placeService;

    @Mock
    private PlaceMapper placeMapper;

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @InjectMocks
    private CloudinaryServiceImpl cloudinaryService;

    @Test
    void uploadPhoto_WhenUploadSucceeds_ShouldSaveAndReturnPhoto() throws IOException {
        Long placeId = 42L;
        byte[] bytes = "image-bytes".getBytes(StandardCharsets.UTF_8);
        String contentType = "image/png";
        long fileSize = 123L;

        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenReturn(bytes);
        when(file.getContentType()).thenReturn(contentType);
        when(file.getSize()).thenReturn(fileSize);

        PlaceDto placeDto = new PlaceDto();
        placeDto.setName("Place name");
        placeDto.setDescription("Place description");
        when(placeService.findPlaceById(placeId)).thenReturn(placeDto);

        Place place = new Place();
        place.setName(placeDto.getName());
        place.setDescription(placeDto.getDescription());
        when(placeMapper.toPlace(placeDto)).thenReturn(place);

        when(cloudinary.uploader()).thenReturn(uploader);
        Map<String, Object> uploadResult = new HashMap<>();
        uploadResult.put("secure_url", "https://example.org/photo.png");
        uploadResult.put("public_id", "places/42/photo123");
        when(uploader.upload(any(byte[].class), anyMap())).thenReturn(uploadResult);

        Photo savedPhoto = Photo.builder()
                .id(1L)
                .url(uploadResult.get("secure_url").toString())
                .publicId(uploadResult.get("public_id").toString())
                .fileSize(fileSize)
                .mediaType(contentType)
                .place(place)
                .build();
        when(photoService.savePhoto(any(Photo.class))).thenReturn(savedPhoto);

        Photo result = cloudinaryService.uploadPhoto(file, placeId);

        assertEquals(savedPhoto, result);

        ArgumentCaptor<byte[]> bytesCaptor = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<Map> optionsCaptor = ArgumentCaptor.forClass(Map.class);
        verify(uploader).upload(bytesCaptor.capture(), optionsCaptor.capture());
        assertArrayEquals(bytes, bytesCaptor.getValue());

        Map<String, Object> options = optionsCaptor.getValue();
        assertEquals("places/" + placeId, options.get("folder"));
        assertEquals("image", options.get("resource_type"));

        ArgumentCaptor<Photo> photoCaptor = ArgumentCaptor.forClass(Photo.class);
        verify(photoService).savePhoto(photoCaptor.capture());
        Photo photoToSave = photoCaptor.getValue();

        assertNotNull(photoToSave.getPlace());
        assertEquals(placeId, photoToSave.getPlace().getId());
        assertEquals(uploadResult.get("secure_url"), photoToSave.getUrl());
        assertEquals(uploadResult.get("public_id"), photoToSave.getPublicId());
        assertEquals(fileSize, photoToSave.getFileSize());
        assertEquals(contentType, photoToSave.getMediaType());
    }

    @Test
    void uploadPhoto_WhenCloudinaryThrowsIOException_ShouldThrowPhotoUploadException() throws IOException {
        Long placeId = 42L;
        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenReturn("bytes".getBytes(StandardCharsets.UTF_8));

        PlaceDto placeDto = new PlaceDto();
        when(placeService.findPlaceById(placeId)).thenReturn(placeDto);
        when(placeMapper.toPlace(placeDto)).thenReturn(new Place());

        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(byte[].class), anyMap())).thenThrow(new IOException("I/O failure"));

        PhotoUploadException exception = assertThrows(
                PhotoUploadException.class,
                () -> cloudinaryService.uploadPhoto(file, placeId)
        );

        assertEquals("Failed to upload photo to Cloudinary", exception.getMessage());
        verify(photoService, never()).savePhoto(any(Photo.class));
    }

    @Test
    void deletePhoto_WhenDestroySucceeds_ShouldCallCloudinaryDestroy() throws IOException {
        String publicId = "places/42/photo123";
        when(cloudinary.uploader()).thenReturn(uploader);

        cloudinaryService.deletePhoto(publicId);

        ArgumentCaptor<Map> optionsCaptor = ArgumentCaptor.forClass(Map.class);
        verify(uploader).destroy(eq(publicId), optionsCaptor.capture());
        assertTrue(optionsCaptor.getValue().isEmpty());
    }

    @Test
    void deletePhoto_WhenCloudinaryThrowsIOException_ShouldThrowPhotoUploadException() throws IOException {
        String publicId = "places/42/photo123";
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.destroy(eq(publicId), anyMap())).thenThrow(new IOException("I/O failure"));

        PhotoUploadException exception = assertThrows(
                PhotoUploadException.class,
                () -> cloudinaryService.deletePhoto(publicId)
        );

        assertEquals("Failed to delete photo from Cloudinary", exception.getMessage());
    }
}

