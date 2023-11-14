package io.project.BorovskBot.repository;

import io.project.BorovskBot.model.Ad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdsRepository extends JpaRepository<Ad,Long> {
}
