package test.com.pmrodrigues.rpg.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.pmrodrigues.rpg.entities.User;
import com.pmrodrigues.rpg.repositories.UserRepository;

import test.com.pmrodrigues.rpg.TestApplication;
import test.com.pmrodrigues.rpg.factories.UserFactory;

@SpringBootTest(classes = TestApplication.class, webEnvironment = WebEnvironment.NONE)
@TestInstance(Lifecycle.PER_CLASS)
public class UserRepositoryTest {

	
	@Autowired
	public UserRepository userRepository;

	@Autowired
	public UserFactory userFactory;

	private User author;
	
	@BeforeEach
	public void setup() {
		
		this.author = userFactory.buildNew().save().getUser();

		
	}
	
	@AfterEach
	public void cleanUp() {
		userRepository.delete(author);		
	}
	
	@Test
	public void shouldFound() {
		
		var author = userRepository.findByEmail(this.author.getEmail());
		assertTrue(author.isPresent());
		assertEquals(this.author, author.get());
		
	}
	
	@Test
	public void shouldNotFound() {
		
		var author = userRepository.findByEmail("any@not-found.com");
		assertTrue(author.isEmpty());
		
	}
	
	
	
}
