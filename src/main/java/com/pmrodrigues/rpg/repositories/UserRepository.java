package com.pmrodrigues.rpg.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.pmrodrigues.rpg.entities.User;

@Repository
@RepositoryRestResource(exported = false)
public interface UserRepository extends CrudRepository<User, UUID>{

	@Query(value = "SELECT u from User u left join fetch u.authorities a where u.email = :email")
	Optional<User> findByEmail(@Param("email") final String email);
	

}
