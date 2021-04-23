package com.pmrodrigues.rpg.services.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.pmrodrigues.rpg.repositories.UserRepository;

import io.micrometer.core.lang.NonNull;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
		
		log.info("try to find a user with {}" , username);
		
		var user = repository.findByEmail(username);
		
		if( user.isPresent() ) {
			var founded =  user.get();
			if( log.isDebugEnabled() ) {
				log.debug("user {} founded",founded);				
			}
			return founded;
		}
		
		log.info("username {} not founded" , username);
		return null;
		
	}

}
