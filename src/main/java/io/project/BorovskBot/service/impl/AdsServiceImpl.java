package io.project.BorovskBot.service.impl;

import io.project.BorovskBot.model.Ads;
import io.project.BorovskBot.repository.AdsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class AdsServiceImpl implements io.project.BorovskBot.service.AdsService {
    private final AdsRepository adsRepository;

    @Override
    public List<Ads> findAllAds() {
        return adsRepository.findAll();
    }
}
