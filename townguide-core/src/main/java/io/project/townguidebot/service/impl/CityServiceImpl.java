package io.project.townguidebot.service.impl;

import io.project.townguidebot.dto.request.CityCreateRq;
import io.project.townguidebot.dto.response.CityResponse;
import io.project.townguidebot.exception.CityNotFoundException;
import io.project.townguidebot.mapper.CityMapper;
import io.project.townguidebot.model.City;
import io.project.townguidebot.repository.CityRepository;
import io.project.townguidebot.service.CityService;
import io.project.townguidebot.service.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.project.townguidebot.model.enums.ButtonCallback.CITY;
import static io.project.townguidebot.service.constants.Prefixes.CITY_PREFIX;

@Service
@Slf4j
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    private final Map<Long, String> cityForChat = new ConcurrentHashMap<>();

    @Override
    @Transactional(readOnly = true)
    public List<CityResponse> getAllCity() {
        log.debug("Get all cities");
        return cityMapper.toListCityResponse(cityRepository.findAll());
    }

    @Override
    public String getSelectedCityForChat(Long chatId) {
        log.debug("Get selected city for chat: {}", chatId);
        return cityForChat.get(chatId);
    }

    @Override
    public void unselectedCityForChat(Long chatId) {
        cityForChat.remove(chatId);
    }

    @Override
    @Transactional(readOnly = true)
    public City findCityById(Long id) {
        return cityRepository.findById(id).orElseThrow(()->{
                    log.warn("City with id: {} not found", id);
                    return new CityNotFoundException(String.format("City with id: %s not found", id));
                });
    }

    @Override
    public String selectedCity(String callbackData, Long chatId) {
        String cityName;
        if (callbackData.startsWith(CITY_PREFIX)) {
            cityName = callbackData.substring(CITY_PREFIX.length());
        } else {
            return cityForChat.get(chatId);
        }
        log.debug("Select city: {} for chat: {}", cityName, chatId);
        String name = cityForChat.putIfAbsent(chatId, cityName);
        return name == null ? cityForChat.get(chatId) : name;
    }

    @Override
    @Transactional(readOnly = true)
    public String getDescriptionSelectedCity(Long chatId) {
        String nameCity = getSelectedCityForChat(chatId);
        log.debug("Find city with name: {} in database", nameCity);
        return cityRepository.findDescriptionByNameEng(nameCity).orElseThrow(()->{
            log.warn("City with name: {} not found", nameCity);
            return new CityNotFoundException(String.format("City with name: %s not found", nameCity));
        }
        );
    }

    @Override
    @Transactional(readOnly = true)
    public String getCityNameByNameEng(String nameEng) {
        log.debug("Find name city by english name: {}", nameEng);
        return cityRepository.findCityNameByNameEng(nameEng).orElseThrow(()->{
            log.warn("Name city for english name: {} not found", nameEng);
            return new CityNotFoundException(String.format("City with name: %s not found", nameEng));
        });
    }

    /**
     * Создание города без фотографии, места и истории
     *
     * @param req {@link CityCreateRq}
     * @return {@link CityResponse}
     */
    @Override
    public CityResponse create(CityCreateRq req) {
        log.debug("Create city: {}", req.getName());
        City city = cityRepository.save(City.builder()
            .name(req.getName())
            .nameEng(req.getNameEng())
            .description(req.getDescription())
            .callback(CITY.toString())
            .build());
        return cityMapper.toCityResponse(city);
    }
}
