package io.project.BorovskBot.repository;

import io.project.BorovskBot.model.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {
    @Query(value = "select * from stories order by random() limit 1", nativeQuery = true)
    Story findRandomStory();
}
