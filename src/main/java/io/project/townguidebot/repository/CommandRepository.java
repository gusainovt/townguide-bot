package io.project.townguidebot.repository;

import io.project.townguidebot.model.Command;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandRepository extends JpaRepository<Command,Long> {
}
