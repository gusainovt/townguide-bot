package io.project.townguidebot.repository;

import io.project.townguidebot.model.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdsRepository extends JpaRepository<Ad,Long> {
}
