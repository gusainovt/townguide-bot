package io.project.townguidebot.service;

import io.project.townguidebot.dto.request.CityCreateRq;
import io.project.townguidebot.dto.response.CityResponse;
import io.project.townguidebot.model.City;

import java.util.List;

public interface CityService {

    List<CityResponse> getAllCity();

    String getSelectedCityForChat(Long chatId);

    void unselectedCityForChat(Long chatId);

    City findCityById(Long id);

    String selectedCity(String callbackData, Long chatId);

    String getDescriptionSelectedCity(Long chatId);

    String getCityNameByNameEng(String nameEng);

    CityResponse create(CityCreateRq req);
}
