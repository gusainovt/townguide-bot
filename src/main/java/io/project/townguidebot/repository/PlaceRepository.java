package io.project.townguidebot.repository;

import io.project.townguidebot.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    @Query(value = "select * from places order by random() limit 1", nativeQuery = true)
    Optional<Place> findRandomPlace();
}
