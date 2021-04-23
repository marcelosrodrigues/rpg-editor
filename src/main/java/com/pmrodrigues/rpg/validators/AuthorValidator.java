package com.pmrodrigues.rpg.validators;

import static java.lang.String.format;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import com.pmrodrigues.rpg.entities.Author;
import com.pmrodrigues.rpg.exceptions.DuplicatedKeyException;
import com.pmrodrigues.rpg.repositories.AuthorRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RepositoryEventHandler(Author.class)
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class AuthorValidator{
	
	@Autowired
	private AuthorRepository repository;

	@HandleBeforeCreate
	public void onBeforeCreate(final Author author) {
		
		log.info(format("trying to save object %s on repository", author));
		
		var existed = repository.findByEmail(author.getEmail());
		if( existed != null ) {
			throw new DuplicatedKeyException("There is other author with same email saved on database");
		}

	}
	
	@HandleBeforeSave
	public void onBeforeSave(final Author author) {
		
		log.info(format("trying to save object %s on repository", author));
		
		var existed = repository.findByEmail(author.getEmail());
		if( existed != null && !author.equals(existed) ) {
			throw new DuplicatedKeyException("There is other author with same email saved on database");
		}
		
	}

}
