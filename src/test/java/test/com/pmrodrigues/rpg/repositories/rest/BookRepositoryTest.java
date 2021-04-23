package test.com.pmrodrigues.rpg.repositories.rest;

import com.pmrodrigues.rpg.entities.*;
import com.pmrodrigues.rpg.repositories.BookRepository;
import com.pmrodrigues.rpg.repositories.GenreRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import test.com.pmrodrigues.rpg.TestApplication;
import test.com.pmrodrigues.rpg.factories.*;
import test.com.pmrodrigues.rpg.helper.ConnectionUtilities;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import static java.lang.String.format;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = TestApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class BookRepositoryTest {

	private ConnectionUtilities connectionUtilies = new ConnectionUtilities(Book.class);

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private GenreRepository genreRepository;

	@Autowired
	private RuleFactory ruleFactory;

	@Autowired
	private UserFactory userFactory;

	@Autowired
	private AuthorFactory authorFactory;

	@Autowired
	private BookFactory bookFactory;

	@Autowired
	private GenreFactory genreFactory;

	private Author author;
	private User adminUser;
	private Author other;

	private List<Genre> categories;

	@BeforeAll
	public void start() {

		var adminRule = ruleFactory.getRule(BasicSecurityGroups.ADMIN);
		var authorRule = ruleFactory.getRule(BasicSecurityGroups.AUTHORS);

		this.adminUser = userFactory
				.buildNew()
				.withEmail("admin@admin.com")
				.withExpiredAccountDate(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
				.withExpiredCredentialDate(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
				.withRole(adminRule)
				.save()
				.getUser();
		
		this.author = authorFactory
				.buildNew()
				.withRule(authorRule)
				.withExpiredAccountDate(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
				.withExpiredCredentialDate(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
				.save()
				.getAuthor();

		this.other = authorFactory
				.buildNew()
				.withRule(authorRule)
				.withEmail("other@teste.com")
				.withExpiredAccountDate(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
				.withExpiredCredentialDate(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
				.getAuthor();

		this.categories = genreFactory.createNewList();

		connectionUtilies.cleanUpSMTPServer();
		connectionUtilies = connectionUtilies.auth(this.author);
	}




	@AfterAll
	public void stop() {

		var books = bookRepository.findAll();
		bookRepository.deleteAll(books);
		connectionUtilies.stopSMTPServer();
	}

	@AfterEach
	public void after() {

		Page<Book> books = bookRepository.findByAuthor(this.author, PageRequest.of(0, 10));
		bookRepository.deleteAll(books);

	}

	@Test
	public void shouldInsert() {

		var index = nextInt(0, 2);
		var category = this.categories.get(index);

		var book = bookFactory.buildNew()
									.withAuthor(author)
									.withGenre(category)
									.withDescription("TESTE")
									.withName("TESTE")
									.createMessage();

		var response = connectionUtilies.auth(adminUser).post("books", book);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		var saved = (Book)response.getBody();
		Assertions.assertNotNull(saved.getId());
	}

	@Test
	public void shouldUpdate() {

		var index = nextInt(0, 2);
		var category = this.categories.get(index);

		var request = bookFactory.buildNew()
				.withAuthor(author)
				.withGenre(category)
				.withDescription("TESTE")
				.withName("TESTE")
				.createMessage();

		var response = connectionUtilies.auth(adminUser).post("books", request);

		var book = (Book)response.getBody();

		index = nextInt(0, 2);
		category = this.categories.get(index);

		request = bookFactory.withGenre(category).createMessage();
		response = connectionUtilies.auth(adminUser).put(format("books/%s", book.getId()), request);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void shouldUpdateWithSameAuthor() {

		var index = nextInt(0, 2);
		var category = this.categories.get(index);

		var request = bookFactory.buildNew()
				.withAuthor(author)
				.withGenre(category)
				.withDescription("TESTE")
				.withName("TESTE")
				.createMessage();

		var response = connectionUtilies.auth(adminUser).post("books", request);

		var book = (Book)response.getBody();

		index = nextInt(0, 2);
		category = this.categories.get(index);


		request = bookFactory.withGenre(category).createMessage();
		response = connectionUtilies.auth(author).put(format("books/%s", book.getId()), request);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void getABook() {

		var index = nextInt(0, 2);
		var category = this.categories.get(index);

		var request = bookFactory.buildNew()
				.withAuthor(author)
				.withGenre(category)
				.withDescription("TESTE")
				.withName("TESTE")
				.createMessage();

		var response = connectionUtilies.auth(adminUser).post("books", request);

		var book = (Book)response.getBody();

		var saved = connectionUtilies.auth(adminUser).get(format("books/%s", book.getId()));

		assertEquals(saved.getBody(), book);

	}

	@Test
	public void findBooksByName() {

		var index = nextInt(0, 2);
		var category = this.categories.get(index);

		var request = bookFactory.buildNew()
				.withAuthor(author)
				.withGenre(category)
				.withDescription("TESTE")
				.withName("TESTE")
				.createMessage();

		var response = connectionUtilies.auth(adminUser).post("books", request);
		
		var book =(Book) response.getBody();
		
		var saved = connectionUtilies.auth(adminUser).get(format("books/%s", book.getId()));

		assertEquals(HttpStatus.OK, saved.getStatusCode());

	}

	@Test
	public void shouldFindBooksWithPagination() {

		for (int i = 0; i < 30; i++) {
			var index = nextInt(0, 2);
			var category = this.categories.get(index);

			var request = bookFactory.buildNew()
					.withAuthor(author)
					.withGenre(category)
					.withDescription("TESTE")
					.withName("TESTE")
					.createMessage();

			connectionUtilies.auth(adminUser).post("books", request);

		}
		
		var saved = connectionUtilies.auth(adminUser).get(format("books/search/findByAuthor?author=%s&page=1&size=5&sort=category.id,asc", author.getId()));

		assertEquals(HttpStatus.OK, saved.getStatusCode());

	}
	
	@Test
	public void shouldListBooksByCategory() {
		
		var category = genreFactory.buildNew().withName("Horror").save().getCategory();
		var subCategories = genreFactory.createNewList();
		category.addSubGenres( subCategories );
		genreRepository.save(category);

		var authentication = new UsernamePasswordAuthenticationToken(adminUser , adminUser.getPassword(), adminUser.getAuthorities());
		var context = SecurityContextHolder.getContext();
		context.setAuthentication(authentication);
		
		
		for( var subCategory : category.getSubGenres() ) {
			
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
		
		
		var saved = connectionUtilies.get(format("books/search/findByGenre?path=%s&page=1&size=5&sort=category.id,asc", category.getPath() ));

		assertEquals(HttpStatus.OK, saved.getStatusCode());

	}
	

}
