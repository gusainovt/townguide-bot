package io.project.BorovskBot.repository;

import io.project.BorovskBot.model.Ads;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdsRepository extends JpaRepository<Ads,Long> {
}
