package io.project.BorovskBot.service;

import io.project.BorovskBot.model.Ad;

import java.util.List;

public interface AdsService {
    List<Ad> findAllAds();
    Ad findAdById(long id);
    Ad createAd(Ad ad);
    Ad updateAd(long id, Ad ad);
    void deleteAd(long id);
}
