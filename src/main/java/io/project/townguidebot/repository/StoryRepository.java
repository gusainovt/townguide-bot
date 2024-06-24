package io.project.townguidebot.repository;

import io.project.townguidebot.model.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {
    @Query(value = "select * from stories order by random() limit 1", nativeQuery = true)
    Optional<Story> findRandomStory();
}
