package io.project.BorovskBot.repository;

import io.project.BorovskBot.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {

}
