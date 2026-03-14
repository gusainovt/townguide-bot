package io.project.townguidebot.mapper;

import io.project.townguidebot.dto.request.PlaceCreateRq;
import io.project.townguidebot.dto.response.PlaceCreateRs;
import io.project.townguidebot.model.Place;
import io.project.townguidebot.dto.PlaceDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PlaceMapper {

    @Mapping(target = "cityId", ignore = true)
    PlaceCreateRq to(Place req);

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "photo", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "city", ignore = true)
    Place to(PlaceCreateRq req);

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "photo", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "city", ignore = true)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    Place toPlace(PlaceDto placeDto);

    @Mapping(source = "city.id", target = "cityId")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    PlaceDto toPlaceDto(Place place);

    List<PlaceDto> toListPlacesDto(List<Place> places);

    @Mapping(source = "city.id", target = "cityId")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    PlaceCreateRs toRs(Place place);
}
