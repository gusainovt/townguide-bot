package io.project.BorovskBot.service.impl;

import io.project.BorovskBot.model.Place;
import io.project.BorovskBot.repository.PlaceRepository;
import org.springframework.stereotype.Service;

@Service
public class PlaceServiceImpl implements io.project.BorovskBot.service.PlaceService {
    private final PlaceRepository placeRepository;

    public PlaceServiceImpl(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Override
    public String addPlace(String name, String description) {
        Place place = new Place(name, description);
        placeRepository.save(place);
        return "Место успешно добавлено";
    }

}
