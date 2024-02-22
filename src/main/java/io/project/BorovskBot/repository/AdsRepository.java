package io.project.BorovskBot.repository;

import io.project.BorovskBot.model.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdsRepository extends JpaRepository<Ad,Long> {
}
