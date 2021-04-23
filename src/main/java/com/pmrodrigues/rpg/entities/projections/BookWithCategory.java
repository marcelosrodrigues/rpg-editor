package com.pmrodrigues.rpg.entities.projections;

import java.util.UUID;

import org.springframework.data.rest.core.config.Projection;

import com.pmrodrigues.rpg.entities.Book;
import com.pmrodrigues.rpg.entities.Genre;

@Projection(name = "with-category", types = Book.class)
public interface BookWithCategory {

	UUID getId();
	
	String getName();
	
	String getDescription();
	
	Genre getCategory();
	
}
