package io.project.townguidebot.service.impl;

import io.project.townguidebot.exception.CityNotFoundException;
import io.project.townguidebot.model.City;
import io.project.townguidebot.repository.CityRepository;
import io.project.townguidebot.service.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    private final Map<Long, String> cityForChat = new ConcurrentHashMap<>();

    @Override
    @Transactional(readOnly = true)
    public List<City> getAllCity() {
        log.info("Get all cities...");
        return cityRepository.findAll();
    }

    @Override
    public String getSelectedCityForChat(Long chatId) {
        log.info("Get selected city for chat: {}", chatId);
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
                    log.error("City with id: {} not found", id);
                    return new CityNotFoundException(String.format("City with id: %s not found", id));
                });
    }

    @Override
    public String selectedCity(String callbackData, Long chatId) {
        String cityName;
        if (callbackData.contains(":")) {
            cityName = callbackData.split(":", 2)[1];
        } else {
            return cityForChat.get(chatId);
        }
        log.info("Select city: {} for chat: {}", cityName, chatId);
        String name = cityForChat.putIfAbsent(chatId, cityName);
        return name == null ? cityForChat.get(chatId) : name;
    }

    @Override
    @Transactional(readOnly = true)
    public String getDescriptionSelectedCity(Long chatId) {
        String nameCity = getSelectedCityForChat(chatId);
        log.info("Find city with name: {} in database", nameCity);
        return cityRepository.findDescriptionByNameEng(nameCity).orElseThrow(()->{
            log.error("City with name: {} not found", nameCity);
            return new CityNotFoundException(String.format("City with name: %s not found", nameCity));
        }
        );
    }

    @Override
    @Transactional(readOnly = true)
    public String getCityNameByNameEng(String nameEng) {
        log.info("Find name city by english name: {}", nameEng);
        return cityRepository.findCityNameByNameEng(nameEng).orElseThrow(()->{
            log.error("Name city for english name: {} not found", nameEng);
            return new CityNotFoundException(String.format("City with name: %s not found", nameEng));
        });
    }


}
