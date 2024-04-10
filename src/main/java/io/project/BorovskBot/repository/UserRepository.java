package io.project.BorovskBot.repository;

import io.project.BorovskBot.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
