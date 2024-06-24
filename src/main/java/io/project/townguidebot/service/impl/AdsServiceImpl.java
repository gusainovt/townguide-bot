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

import static io.project.townguidebot.service.constants.ErrorText.ERROR_AD_NOT_FOUND;
import static io.project.townguidebot.service.constants.ErrorText.ERROR_TEXT;
import static io.project.townguidebot.service.constants.LogText.METHOD_CALLED;
import static io.project.townguidebot.service.constants.LogText.WITH_ID;

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
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
        return adMapper.toListAdsDto(adsRepository.findAll());
    }

    /**
     * Находит объявление по ID
     * @param id идентификатор объявления
     * @return найденное объявление {@link Ad}
     */
    @Override
    public AdDto findAdById(long id) {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName() + WITH_ID + id);
        return adMapper.toAdDto(adsRepository.findById(id).orElseThrow(() -> {
            AdNotFoundException adEx = new AdNotFoundException(String.format(ERROR_AD_NOT_FOUND, id));
            log.error(ERROR_TEXT + adEx.getMessage());
            return adEx;
        }));
    }

    /**
     * Создает новое объявление
     * @param adDto объект объявления {@link Ad}
     * @return созданное объявление {@link Ad}
     */
    @Override
    public AdDto createAd(AdDto adDto) {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
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
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName() + WITH_ID + id);
        if (adsRepository.existsById(id)) {
            Ad ad = adMapper.ToAd(adDto);
            ad.setId(id);
            return adMapper.toAdDto(adsRepository.save(ad));
        } else {
            String message = String.format(ERROR_AD_NOT_FOUND, id);
            log.error(ERROR_TEXT + message);
            throw new AdNotFoundException(message);
        }
    }

    /**
     * Удаляет объявление по ID
     * @param id идентификатор объявления
     */
    @Override
    public void deleteAd(long id) {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName() + WITH_ID + id);
        if (adsRepository.existsById(id)) {
            adsRepository.deleteById(id);
        } else {
            String message = String.format(ERROR_AD_NOT_FOUND, id);
            log.error(ERROR_TEXT + message);
            throw new AdNotFoundException(message);
        }
    }
}
