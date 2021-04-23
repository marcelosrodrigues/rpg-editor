package com.pmrodrigues.rpg.entities.projections;

import java.util.UUID;

import org.springframework.data.rest.core.config.Projection;

import com.pmrodrigues.rpg.entities.Author;
import com.pmrodrigues.rpg.entities.Book;
import com.pmrodrigues.rpg.entities.Genre;

@Projection(name = "book-all-information", types = Book.class)
public interface BookWithAll {

	UUID getId();
	
	String getName();
	
	String getDescription();
	
	Author getAuthor();
	
	Genre getCategory();
	
}
