package test.com.pmrodrigues.rpg.repositories.rest;

import static java.lang.String.format;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import com.pmrodrigues.rpg.entities.Author;
import com.pmrodrigues.rpg.entities.User;
import com.pmrodrigues.rpg.repositories.AuthorRepository;
import com.pmrodrigues.rpg.repositories.UserRepository;

import test.com.pmrodrigues.rpg.TestApplication;
import test.com.pmrodrigues.rpg.factories.AuthorFactory;
import test.com.pmrodrigues.rpg.factories.UserFactory;
import test.com.pmrodrigues.rpg.helper.ConnectionUtilities;

@SpringBootTest(classes = TestApplication.class,
webEnvironment = WebEnvironment.DEFINED_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class AuthorRepositoryTest {
	
	@Autowired
	private JdbcTemplate template;
	
	@Autowired
	private AuthorRepository repository;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserFactory userFactory;

	@Autowired
	private AuthorFactory authorFactory;
		
	private ConnectionUtilities connectionUtilities = new ConnectionUtilities(Author.class);

	private User user;
	
	@BeforeAll
	public void start() {
		this.user = userFactory
				.buildNew()
				.withEmail("admin@admin.com")
				.withExpiredAccountDate(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
				.withExpiredCredentialDate(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
				.save()
				.getUser();

		connectionUtilities.cleanUpSMTPServer();
	}
	
	@AfterAll
	public void stop() {
		connectionUtilities.stopSMTPServer();
		userRepository.delete(user);
	}
	
	@AfterEach
	@BeforeEach
	public void cleanUp() {
		connectionUtilities.cleanUpSMTPServer();
		template.update("delete from user where email like '%teste.com%'");		
	}
	
	@Test
	public void shouldInsert() throws Exception {
		
		var author = authorFactory
						.buildNew()						
						.getAuthor();
		
		var response = connectionUtilities.auth(user).post("authors" , author);
				
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
		var saved = repository.findByEmail(author.getEmail());
		
		Assertions.assertNotNull(saved.getId());
		Assertions.assertNotNull(saved.getPassword());
		
		Assertions.assertTrue(connectionUtilities.isEmailReceived(5000, 1));
        
		List<MimeMessage> messages = connectionUtilities.messagesSent();
        assertFalse(messages.isEmpty());
        assertEquals(1, messages.size());
        
        
        var message = messages.get(0);
        
        assertEquals("Welcome to RPG Editor", message.getSubject());       
          
			
	}
	
	@Test
	public void getAuthor() {
		
		var author = authorFactory
				.buildNew()						
				.getAuthor();
		
		var response = connectionUtilities.post("authors" , author);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
		var saved = repository.findByEmail(author.getEmail());
		
		var readed = connectionUtilities.get(format("authors/%s",saved.getId()));
		
		assertEquals(saved.getId(), ((Author)readed.getBody()).getId());
		
	}
	
	@Test
	public void findByEmail() {
		
		var author = authorFactory
				.buildNew()						
				.getAuthor();
		
		var response = connectionUtilities.post("authors" , author);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
		var saved = repository.findByEmail(author.getEmail());
		
		var readed = connectionUtilities.get(format("authors/search/findByEmail?email=%s", saved.getEmail())); 
		
		assertEquals(saved.getId(), ((Author)readed.getBody()).getId());

		
	}
	
	@Test
	@Transactional
	public void shouldUpdate() {
		var author = authorFactory
				.buildNew()						
				.getAuthor();
		
		var response = connectionUtilities.auth(user).post("authors" , author);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
		var saved = repository.findByEmail(author.getEmail());
		
		saved.setName("UPDATED");
		
		response = connectionUtilities.put(format("authors/%s",saved.getId()), saved);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void shoudntInsert() {
		
		var author = authorFactory
				.buildNew()						
				.getAuthor();
		
		var response = connectionUtilities.auth(user).post("authors" , author);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
		try {
			var other = authorFactory
					.buildNew()						
					.getAuthor();
			
			response = connectionUtilities.post("authors" , other);
		
		}catch(HttpClientErrorException ex) {
			assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
		}
		
	}
	
	@Test
	@Transactional
	public void shouldntUpdate() {
		
		var author = authorFactory
				.buildNew()
				.withEmail("shouldnt-update@teste.com")
				.getAuthor();
		
		var response = connectionUtilities.auth(user).post("authors" , author);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
		try {
			
			var other = authorFactory
					.buildNew()						
					.getAuthor();
			
			response = connectionUtilities.post("authors" , other);
			assertEquals(HttpStatus.CREATED, response.getStatusCode());
			
			var saved = repository.findByEmail(other.getEmail());
			
			saved.setEmail(author.getEmail());
			response = connectionUtilities.put(format("authors/%s",saved.getId()), saved);
		
		}catch(HttpClientErrorException ex) {
			assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
		}
		
	}
	
}
