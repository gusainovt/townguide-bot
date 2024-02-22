package io.project.BorovskBot.service;

import io.project.BorovskBot.model.dto.AdDto;

import java.util.List;

public interface AdsService {
    List<AdDto> findAllAds();
    AdDto findAdById(long id);
    AdDto createAd(AdDto adDto);
    AdDto updateAd(long id, AdDto adDto);
    void deleteAd(long id);
}
