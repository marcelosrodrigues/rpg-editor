package com.pmrodrigues.rpg.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pmrodrigues.rpg.entities.Author;

@Repository
public interface AuthorRepository extends CrudRepository<Author, UUID> {

	@Query(value = "from Author a where a.email = :email")
	Author findByEmail(@Param("email") final String email);
	
}
