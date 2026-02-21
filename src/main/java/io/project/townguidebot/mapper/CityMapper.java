package io.project.townguidebot.mapper;

import io.project.townguidebot.dto.response.CityResponse;
import io.project.townguidebot.model.City;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CityMapper {

  List<CityResponse> toListCityResponse(List<City> cities);

  CityResponse toCityResponse(City city);
}
