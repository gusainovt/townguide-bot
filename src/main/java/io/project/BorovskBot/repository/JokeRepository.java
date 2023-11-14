package io.project.BorovskBot.repository;

import io.project.BorovskBot.model.Joke;
import org.springframework.data.repository.CrudRepository;

public interface JokeRepository extends CrudRepository<Joke, Integer> {

}
