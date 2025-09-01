package io.project.townguidebot.service.impl;

import io.project.townguidebot.model.City;
import io.project.townguidebot.repository.CityRepository;
import io.project.townguidebot.service.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Override
    public List<City> getAllCity() {
        log.info("Get all cities...");
        return cityRepository.findAll();
    }
}
