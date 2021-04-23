package test.com.pmrodrigues.rpg.services.security;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pmrodrigues.rpg.entities.User;
import com.pmrodrigues.rpg.repositories.UserRepository;
import com.pmrodrigues.rpg.services.security.UserDetailsServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceTest {
	
	
	@Mock
	private UserRepository repository;
	
	@InjectMocks
	private UserDetailsServiceImpl service;
	
	
	@Test
	public void shouldFound() {
		
		
		when(repository.findByEmail((String)notNull())).thenReturn(Optional.of(new User()));
		
		var user = service.loadUserByUsername("teste@teste.com");
		Assertions.assertNotNull(user);
		
	}
	
	@Test
	public void shouldNotFound() {
		
		when(repository.findByEmail((String)notNull())).thenReturn(Optional.empty());
		
		var user = service.loadUserByUsername("teste@teste.com");
		Assertions.assertNull(user);
	}

}
