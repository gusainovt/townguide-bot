package io.project.townguidebot.repository;

import io.project.townguidebot.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City,Long> {

    @Query("SELECT c.description FROM City c WHERE c.nameEng = :nameEng")
    Optional<String> findDescriptionByNameEng(@Param("nameEng")String nameEng);

    @Query("select c.name from City c where c.nameEng = :nameEng")
    Optional<String> findCityNameByNameEng(@Param("nameEng") String nameEng);
}
