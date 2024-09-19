package io.project.townguidebot.service;

import io.project.townguidebot.model.City;

import java.util.List;

public interface CityService {

    List<City> getAllCity();

    String getSelectedCityForChat(Long chatId);

    void unselectedCityForChat(Long chatId);

    City findCityById(Long id);

    String selectedCity(String callbackData, Long chatId);

    String getDescriptionSelectedCity(Long chatId);
}
