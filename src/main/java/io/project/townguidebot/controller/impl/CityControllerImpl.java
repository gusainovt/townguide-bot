package io.project.townguidebot.controller.impl;

import io.project.townguidebot.controller.CityController;
import io.project.townguidebot.dto.response.CityResponse;
import io.project.townguidebot.mapper.CityMapper;
import io.project.townguidebot.model.City;
import io.project.townguidebot.service.CityService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@RequiredArgsConstructor
public class CityControllerImpl implements CityController {

  private final CityService cityService;
  private final CityMapper cityMapper;

  @Override
  public List<CityResponse> findAllCities() {
    List<City> cities = cityService.getAllCity();
    return cityMapper.toListCityResponse(cities);
  }
}
