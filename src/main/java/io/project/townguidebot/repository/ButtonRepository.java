package io.project.townguidebot.repository;

import io.project.townguidebot.model.Button;
import io.project.townguidebot.model.ButtonId;
import io.project.townguidebot.model.ButtonType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ButtonRepository extends JpaRepository<Button, ButtonId> {
    List<Button> findAllByButtonIdTypeAndButtonIdLanguageCode(ButtonType type, String languageCode);
}
