package io.project.townguidebot.service;

import io.project.townguidebot.model.dto.AdDto;

import java.util.List;

public interface AdsService {
    List<AdDto> findAllAds();
    AdDto findAdById(long id);
    AdDto createAd(AdDto adDto);
    AdDto updateAd(long id, AdDto adDto);
    void deleteAd(long id);
}
