package io.project.townguidebot.service.impl;

import io.project.townguidebot.exception.PhotoNotFoundException;
import io.project.townguidebot.dto.UploadPhotoResult;
import io.project.townguidebot.model.Photo;
import io.project.townguidebot.model.Place;
import io.project.townguidebot.repository.PhotoRepository;
import io.project.townguidebot.repository.PlaceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PhotoServiceImplTest {

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private PlaceRepository placeRepository;

    @InjectMocks
    private PhotoServiceImpl photoService;

    @Test
    void savePhoto_ShouldSaveAndReturnPhoto() {
        Long placeId = 10L;
        UploadPhotoResult upload = UploadPhotoResult.builder()
                .url("http://example.com/photo.png")
                .fileSize(123L)
                .mediaType("image/png")
                .publicId("places/10/photo123")
                .build();

        Place place = new Place();
        when(placeRepository.getReferenceById(placeId)).thenReturn(place);

        Photo saved = new Photo();
        when(photoRepository.save(org.mockito.ArgumentMatchers.any(Photo.class))).thenReturn(saved);

        Photo result = photoService.savePhoto(placeId, upload);

        assertSame(saved, result);
        verify(placeRepository).getReferenceById(placeId);
        verify(photoRepository).save(org.mockito.ArgumentMatchers.any(Photo.class));
    }

    @Test
    void getAllPhotoByPlace_WhenExists_ShouldReturnList() {
        Long placeId = 10L;
        List<Photo> photos = List.of(new Photo(), new Photo());
        when(photoRepository.findPhotosByPlaceId(placeId)).thenReturn(Optional.of(photos));

        List<Photo> result = photoService.getAllPhotoByPlace(placeId);

        assertSame(photos, result);
        verify(photoRepository).findPhotosByPlaceId(placeId);
    }

    @Test
    void getAllPhotoByPlace_WhenNotFound_ShouldThrowException() {
        Long placeId = 10L;
        when(photoRepository.findPhotosByPlaceId(placeId)).thenReturn(Optional.empty());

        PhotoNotFoundException ex = assertThrows(PhotoNotFoundException.class,
                () -> photoService.getAllPhotoByPlace(placeId));

        assertEquals("Photos not found by place id: 10", ex.getMessage());
        verify(photoRepository).findPhotosByPlaceId(placeId);
    }

    @Test
    void getPhotoUrl_WhenPhotoExists_ShouldReturnUrl() {
        Long photoId = 5L;
        Photo photo = new Photo();
        photo.setUrl("http://example.com/photo.png");
        when(photoRepository.findById(photoId)).thenReturn(Optional.of(photo));

        String result = photoService.getPhotoUrl(photoId);

        assertEquals(photo.getUrl(), result);
        verify(photoRepository).findById(photoId);
    }

    @Test
    void getPhotoUrl_WhenPhotoNotFound_ShouldThrowException() {
        Long photoId = 5L;
        when(photoRepository.findById(photoId)).thenReturn(Optional.empty());

        PhotoNotFoundException ex = assertThrows(PhotoNotFoundException.class,
                () -> photoService.getPhotoUrl(photoId));

        assertEquals("Photo not found: 5", ex.getMessage());
        verify(photoRepository).findById(photoId);
    }
}
