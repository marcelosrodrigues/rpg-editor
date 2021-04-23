package com.pmrodrigues.rpg.repositories;

import com.pmrodrigues.rpg.entities.Genre;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GenreRepository extends CrudRepository<Genre, UUID>{

}
