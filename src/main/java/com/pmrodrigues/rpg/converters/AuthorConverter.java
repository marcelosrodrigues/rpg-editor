package com.pmrodrigues.rpg.converters;

import static java.util.UUID.fromString;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.pmrodrigues.rpg.entities.Author;
import com.pmrodrigues.rpg.repositories.AuthorRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthorConverter implements Converter<URI, Author>{

	@Autowired
	private AuthorRepository repository;
		
	@Override
	public Author convert(URI source) {
		log.info("try to find {} in author repository" , source);
		var author = repository.findById(fromString(source.toString()));

		if( author.isPresent() ) {
			log.info("author {} founded" , author);
			return author.get();
		} else {
			log.info("{} not found in author repository" , source);
			return null;
		}
		
	}

}
