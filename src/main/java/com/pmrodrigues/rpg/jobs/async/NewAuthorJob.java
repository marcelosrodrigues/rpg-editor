package com.pmrodrigues.rpg.jobs.async;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import com.pmrodrigues.rpg.entities.Author;
import com.pmrodrigues.rpg.entities.User;
import com.pmrodrigues.rpg.jobs.EmailJob;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RepositoryEventHandler(Author.class)
public class NewAuthorJob {

	@Autowired
	private EmailJob email;
	
	@Value("${com.pmrodrigues.rpg.email.new_author}")
	private String template;
	
	@HandleAfterCreate
	public void afterCreate(Author author) {
		
		log.info("send a email to {}",author);
		
		var model = new HashMap<String,User>();
		model.put("author", author);
		
		email.sendTO(author, "Welcome to RPG Editor" , template, model);
		
		log.info("email sent succefully to {}",author);
		
	}
}
