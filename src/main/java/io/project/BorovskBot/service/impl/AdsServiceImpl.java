package io.project.BorovskBot.service.impl;

import io.project.BorovskBot.model.Ads;
import io.project.BorovskBot.repository.AdsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static io.project.BorovskBot.service.constants.LogText.METHOD_CALLED;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdsServiceImpl implements io.project.BorovskBot.service.AdsService {

    private final AdsRepository adsRepository;

    /**
     * Находит все объявления в БД
     * @return список объявлений {@link Ads}
     */
    @Override
    public List<Ads> findAllAds() {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
        return adsRepository.findAll();
    }
}
