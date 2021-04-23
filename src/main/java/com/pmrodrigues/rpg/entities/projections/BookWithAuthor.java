package com.pmrodrigues.rpg.entities.projections;

import java.util.UUID;

import org.springframework.data.rest.core.config.Projection;

import com.pmrodrigues.rpg.entities.Author;
import com.pmrodrigues.rpg.entities.Book;

@Projection(name = "with-author", types = Book.class)
public interface BookWithAuthor {

	UUID getId();
	
	String getName();
	
	String getDescription();
	
	Author getAuthor();
	
	
}
