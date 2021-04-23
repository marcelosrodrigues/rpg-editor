package test.com.pmrodrigues.rpg.repositories;


import com.pmrodrigues.rpg.entities.BasicSecurityGroups;
import com.pmrodrigues.rpg.entities.Genre;
import com.pmrodrigues.rpg.entities.Rule;
import com.pmrodrigues.rpg.repositories.BookRepository;
import com.pmrodrigues.rpg.repositories.GenreRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import test.com.pmrodrigues.rpg.TestApplication;
import test.com.pmrodrigues.rpg.factories.AuthorFactory;
import test.com.pmrodrigues.rpg.factories.BookFactory;
import test.com.pmrodrigues.rpg.factories.GenreFactory;
import test.com.pmrodrigues.rpg.factories.RuleFactory;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


@SpringBootTest(classes = TestApplication.class, webEnvironment = WebEnvironment.NONE)
@TestInstance(Lifecycle.PER_CLASS)
public class BookRepositoryTest {

	
	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private GenreRepository genreRepository;

	private Collection<Rule> rules = new ArrayList<>();

	@Autowired
	private RuleFactory ruleFactory;

	private Genre genre;

	@Autowired
	private AuthorFactory authorFactory;

	@Autowired
	private BookFactory bookFactory;

	@Autowired
	private GenreFactory genreFactory;

	@AfterAll
	public void stop() {

		var books = bookRepository.findAll();
		bookRepository.deleteAll(books);
		genreRepository.delete(this.genre);

	}
	
	@BeforeAll
	public void prepareTest() {

		var authorRule = ruleFactory.getRule(BasicSecurityGroups.AUTHORS);
		
		var author = authorFactory.buildNew()
								.withRule(authorRule)
								.save()
								.getAuthor();

		var authentication = new UsernamePasswordAuthenticationToken(author , author.getPassword(), author.getAuthorities());
		var context = SecurityContextHolder.getContext();
		context.setAuthentication(authentication);
		
		this.genre = genreFactory.buildNew()
							.withName("Horror")
							.withSubGenres()
							.save()
							.getCategory();

		
		for( var subCategory : genre.getSubGenres() ) {
			
			for( int i = 0 ; i < 3 ; i++ ) {
				
				var book = bookFactory
							.buildNew()
							.withAuthor(author)
							.withGenre(subCategory)
							.withName(RandomStringUtils.randomAlphabetic(10))
							.withDescription(RandomStringUtils.randomAlphabetic(100))
							.getBook();
				
				bookRepository.save(book);
				
			}		
			
		}
		
	}
	
	@Test
	public void shouldListAllBooks() {
		var books = bookRepository.findByGenre(this.genre.getPath(), PageRequest.of(0, 10));
		assertFalse(books.isEmpty());
		assertEquals(9, books.getNumberOfElements());
	}
	
}
