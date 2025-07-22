package io.project.townguidebot.service.impl;

import io.project.townguidebot.exception.AdNotFoundException;
import io.project.townguidebot.mapper.AdMapper;
import io.project.townguidebot.model.Ad;
import io.project.townguidebot.model.dto.AdDto;
import io.project.townguidebot.repository.AdsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdsServiceImpl implements io.project.townguidebot.service.AdsService {

    private final AdsRepository adsRepository;

    private final AdMapper adMapper;

    /**
     * Находит все объявления в БД
     * @return список объявлений {@link Ad}
     */
    @Override
    public List<AdDto> findAllAds() {
        log.info("Find all ads...");
        return adMapper.toListAdsDto(adsRepository.findAll());
    }

    /**
     * Находит объявление по ID
     * @param id идентификатор объявления
     * @return найденное объявление {@link Ad}
     */
    @Override
    public AdDto findAdById(long id) {
        log.info("Find ad with id: {}", id);
        return adMapper.toAdDto(adsRepository.findById(id).orElseThrow(() -> {
            log.error("Ad with id: {} not found", id);
            return new AdNotFoundException();
        }));
    }

    /**
     * Создает новое объявление
     * @param adDto объект объявления {@link Ad}
     * @return созданное объявление {@link Ad}
     */
    @Override
    public AdDto createAd(AdDto adDto) {
        log.info("Created new ad...");
        Ad ad = adMapper.ToAd(adDto);
        return adMapper.toAdDto(adsRepository.save(ad));
    }

    /**
     * Обновляет существующее объявление
     * @param id идентификатор объявления
     * @param adDto объект объявления {@link Ad}
     * @return обновленное объявление {@link Ad}
     */
    @Override
    public AdDto updateAd(long id, AdDto adDto) {
        log.info("Update ad with id: {}", id);
        if (adsRepository.existsById(id)) {
            Ad ad = adMapper.ToAd(adDto);
            ad.setId(id);
            return adMapper.toAdDto(adsRepository.save(ad));
        } else {
            log.error("Ad with id: {} not found", id);
            throw new AdNotFoundException();
        }
    }

    /**
     * Удаляет объявление по ID
     * @param id идентификатор объявления
     */
    @Override
    public void deleteAd(long id) {
        log.info("Delete ad with id: {}", id);
        if (adsRepository.existsById(id)) {
            adsRepository.deleteById(id);
        } else {
            log.error("Ad with id: {} not found", id);
            throw new AdNotFoundException();
        }
    }
}
