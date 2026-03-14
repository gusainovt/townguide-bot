package io.project.townguidebot.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import io.project.townguidebot.dto.UploadedFile;
import io.project.townguidebot.exception.PhotoUploadException;
import io.project.townguidebot.dto.UploadPhotoResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @InjectMocks
    private CloudinaryServiceImpl cloudinaryService;

    @Test
    void uploadPhoto_WhenUploadSucceeds_ShouldReturnUploadResult() throws IOException {
        Long placeId = 42L;
        byte[] bytes = "image-bytes".getBytes(StandardCharsets.UTF_8);
        String contentType = "image/png";
        UploadedFile file = UploadedFile.builder()
                .bytes(bytes)
                .contentType(contentType)
                .originalFilename("photo.png")
                .build();

        when(cloudinary.uploader()).thenReturn(uploader);
        Map<String, Object> uploadResult = new HashMap<>();
        uploadResult.put("secure_url", "https://example.org/photo.png");
        uploadResult.put("public_id", "places/42/photo123");
        when(uploader.upload(any(byte[].class), anyMap())).thenReturn(uploadResult);

        UploadPhotoResult result = cloudinaryService.uploadPhoto(file, placeId);

        assertNotNull(result);
        assertEquals(uploadResult.get("secure_url").toString(), result.getUrl());
        assertEquals(uploadResult.get("public_id").toString(), result.getPublicId());
        assertEquals((long) bytes.length, result.getFileSize());
        assertEquals(contentType, result.getMediaType());

        ArgumentCaptor<byte[]> bytesCaptor = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<Map> optionsCaptor = ArgumentCaptor.forClass(Map.class);
        verify(uploader).upload(bytesCaptor.capture(), optionsCaptor.capture());
        assertArrayEquals(bytes, bytesCaptor.getValue());

        Map<String, Object> options = optionsCaptor.getValue();
        assertEquals("places/" + placeId, options.get("folder"));
        assertEquals("image", options.get("resource_type"));
    }

    @Test
    void uploadPhoto_WhenCloudinaryThrowsIOException_ShouldThrowPhotoUploadException() throws IOException {
        Long placeId = 42L;
        UploadedFile file = UploadedFile.builder()
                .bytes("bytes".getBytes(StandardCharsets.UTF_8))
                .contentType("image/png")
                .originalFilename("photo.png")
                .build();

        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(byte[].class), anyMap())).thenThrow(new IOException("I/O failure"));

        PhotoUploadException exception = assertThrows(
                PhotoUploadException.class,
                () -> cloudinaryService.uploadPhoto(file, placeId)
        );

        assertEquals("Failed to upload photo to Cloudinary", exception.getMessage());
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
