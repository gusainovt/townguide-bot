package io.project.BorovskBot.service;

import io.project.BorovskBot.model.Ads;

import java.util.List;

public interface AdsService {
    List<Ads> findAllAds();
}
