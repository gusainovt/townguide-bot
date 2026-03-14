package io.project.townguidebot.service.impl;

import io.project.townguidebot.exception.AdNotFoundException;
import io.project.townguidebot.mapper.AdMapper;
import io.project.townguidebot.model.Ad;
import io.project.townguidebot.dto.AdDto;
import io.project.townguidebot.repository.AdsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdsServiceImplTest {

    @Mock
    private AdsRepository adsRepository;

    @Mock
    private AdMapper adMapper;

    @InjectMocks
    private AdsServiceImpl adsService;

    private Ad ad;
    private AdDto adDto;
    private final Long adId = 1L;

    @BeforeEach
    void setUp() {
        ad = new Ad();
        ad.setId(adId);
        ad.setAd("Тестовое объявление");

        adDto = new AdDto();
        adDto.setAd("Тестовое объявление");
    }

    @Test
    void findAllAds_ShouldReturnListOfAds() {
        // Arrange
        List<Ad> ads = Arrays.asList(ad);
        List<AdDto> expectedDtos = Arrays.asList(adDto);
        when(adsRepository.findAll()).thenReturn(ads);
        when(adMapper.toListAdsDto(ads)).thenReturn(expectedDtos);

        // Act
        List<AdDto> result = adsService.findAllAds();

        // Assert
        assertNotNull(result);
        assertEquals(expectedDtos, result);
        verify(adsRepository).findAll();
        verify(adMapper).toListAdsDto(ads);
    }

    @Test
    void findAdById_WhenAdExists_ShouldReturnAd() {
        // Arrange
        when(adsRepository.findById(adId)).thenReturn(Optional.of(ad));
        when(adMapper.toAdDto(ad)).thenReturn(adDto);

        // Act
        AdDto result = adsService.findAdById(adId);

        // Assert
        assertNotNull(result);
        assertEquals(adDto, result);
        verify(adsRepository).findById(adId);
        verify(adMapper).toAdDto(ad);
    }

    @Test
    void findAdById_WhenAdDoesNotExist_ShouldThrowException() {
        // Arrange
        when(adsRepository.findById(adId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AdNotFoundException.class, () -> adsService.findAdById(adId));
        verify(adsRepository).findById(adId);
        verify(adMapper, never()).toAdDto(any());
    }

    @Test
    void createAd_ShouldReturnCreatedAd() {
        // Arrange
        when(adMapper.ToAd(adDto)).thenReturn(ad);
        when(adsRepository.save(ad)).thenReturn(ad);
        when(adMapper.toAdDto(ad)).thenReturn(adDto);

        // Act
        AdDto result = adsService.createAd(adDto);

        // Assert
        assertNotNull(result);
        assertEquals(adDto, result);
        verify(adMapper).ToAd(adDto);
        verify(adsRepository).save(ad);
        verify(adMapper).toAdDto(ad);
    }

    @Test
    void updateAd_WhenAdExists_ShouldReturnUpdatedAd() {
        // Arrange
        when(adsRepository.existsById(adId)).thenReturn(true);
        when(adMapper.ToAd(adDto)).thenReturn(ad);
        when(adsRepository.save(ad)).thenReturn(ad);
        when(adMapper.toAdDto(ad)).thenReturn(adDto);

        // Act
        AdDto result = adsService.updateAd(adId, adDto);

        // Assert
        assertNotNull(result);
        assertEquals(adDto, result);
        verify(adsRepository).existsById(adId);
        verify(adMapper).ToAd(adDto);
        verify(adsRepository).save(ad);
        verify(adMapper).toAdDto(ad);
    }

    @Test
    void updateAd_WhenAdDoesNotExist_ShouldThrowException() {
        // Arrange
        when(adsRepository.existsById(adId)).thenReturn(false);

        // Act & Assert
        assertThrows(AdNotFoundException.class, () -> adsService.updateAd(adId, adDto));
        verify(adsRepository).existsById(adId);
        verify(adMapper, never()).ToAd(any());
        verify(adsRepository, never()).save(any());
    }

    @Test
    void deleteAd_WhenAdExists_ShouldDeleteSuccessfully() {
        // Arrange
        when(adsRepository.existsById(adId)).thenReturn(true);

        // Act
        adsService.deleteAd(adId);

        // Assert
        verify(adsRepository).existsById(adId);
        verify(adsRepository).deleteById(adId);
    }

    @Test
    void deleteAd_WhenAdDoesNotExist_ShouldThrowException() {
        // Arrange
        when(adsRepository.existsById(adId)).thenReturn(false);

        // Act & Assert
        assertThrows(AdNotFoundException.class, () -> adsService.deleteAd(adId));
        verify(adsRepository).existsById(adId);
        verify(adsRepository, never()).deleteById(any());
    }
} 