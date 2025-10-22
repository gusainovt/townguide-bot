package io.project.townguidebot.mapper;

import io.project.townguidebot.model.Place;
import io.project.townguidebot.dto.PlaceDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

import java.util.List;
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PlaceMapper {
    @Mappings({
            @Mapping(target = "photo", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "description", target = "description")
    })
    Place toPlace(PlaceDto placeDto);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    PlaceDto toPlaceDto(Place place);

    List<PlaceDto> toListPlacesDto(List<Place> places);
}
