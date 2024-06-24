package io.project.townguidebot.service.impl;

import io.project.townguidebot.exception.AdNotFoundException;
import io.project.townguidebot.mapper.AdMapper;
import io.project.townguidebot.model.Ad;
import io.project.townguidebot.model.dto.AdDto;
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
    private final Long AD_ID = 1L;

    @BeforeEach
    void setUp() {
        ad = new Ad();
        ad.setId(AD_ID);
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
        when(adsRepository.findById(AD_ID)).thenReturn(Optional.of(ad));
        when(adMapper.toAdDto(ad)).thenReturn(adDto);

        // Act
        AdDto result = adsService.findAdById(AD_ID);

        // Assert
        assertNotNull(result);
        assertEquals(adDto, result);
        verify(adsRepository).findById(AD_ID);
        verify(adMapper).toAdDto(ad);
    }

    @Test
    void findAdById_WhenAdDoesNotExist_ShouldThrowException() {
        // Arrange
        when(adsRepository.findById(AD_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AdNotFoundException.class, () -> adsService.findAdById(AD_ID));
        verify(adsRepository).findById(AD_ID);
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
        when(adsRepository.existsById(AD_ID)).thenReturn(true);
        when(adMapper.ToAd(adDto)).thenReturn(ad);
        when(adsRepository.save(ad)).thenReturn(ad);
        when(adMapper.toAdDto(ad)).thenReturn(adDto);

        // Act
        AdDto result = adsService.updateAd(AD_ID, adDto);

        // Assert
        assertNotNull(result);
        assertEquals(adDto, result);
        verify(adsRepository).existsById(AD_ID);
        verify(adMapper).ToAd(adDto);
        verify(adsRepository).save(ad);
        verify(adMapper).toAdDto(ad);
    }

    @Test
    void updateAd_WhenAdDoesNotExist_ShouldThrowException() {
        // Arrange
        when(adsRepository.existsById(AD_ID)).thenReturn(false);

        // Act & Assert
        assertThrows(AdNotFoundException.class, () -> adsService.updateAd(AD_ID, adDto));
        verify(adsRepository).existsById(AD_ID);
        verify(adMapper, never()).ToAd(any());
        verify(adsRepository, never()).save(any());
    }

    @Test
    void deleteAd_WhenAdExists_ShouldDeleteSuccessfully() {
        // Arrange
        when(adsRepository.existsById(AD_ID)).thenReturn(true);

        // Act
        adsService.deleteAd(AD_ID);

        // Assert
        verify(adsRepository).existsById(AD_ID);
        verify(adsRepository).deleteById(AD_ID);
    }

    @Test
    void deleteAd_WhenAdDoesNotExist_ShouldThrowException() {
        // Arrange
        when(adsRepository.existsById(AD_ID)).thenReturn(false);

        // Act & Assert
        assertThrows(AdNotFoundException.class, () -> adsService.deleteAd(AD_ID));
        verify(adsRepository).existsById(AD_ID);
        verify(adsRepository, never()).deleteById(any());
    }
} 