package io.project.townguidebot.repository;

import io.project.townguidebot.model.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {
    @Query(value = "select s.* from stories s join cities c on s.city_id = c.id where c.name_eng = :nameCity order by random() limit 1", nativeQuery = true)
    Optional<Story> findRandomStoryByNameCity(@Param("nameCity") String nameCity);
}
