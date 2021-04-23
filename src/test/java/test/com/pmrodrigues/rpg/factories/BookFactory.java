package test.com.pmrodrigues.rpg.factories;

import com.pmrodrigues.rpg.entities.Author;
import com.pmrodrigues.rpg.entities.Book;
import com.pmrodrigues.rpg.entities.Genre;
import com.pmrodrigues.rpg.repositories.BookRepository;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public class BookFactory {

	@Autowired
	private BookRepository repository;

	@Getter
	private Book book;

	public BookFactory buildNew() {
		this.book = new Book();
		return this;
	}

	public BookFactory withGenre(@NonNull Genre genre) {
		this.book.setGenre(genre);
		return this;
	}

	public BookFactory withAuthor(@NonNull Author author) {
		this.book.setAuthor(author);
		return this;
	}

	
	public BookFactory withDescription(@NonNull String description) {
		this.book.setDescription(description);
		return this;
	}

	public BookFactory withName(@NonNull String name) {
		this.book.setName(name);
		return this;
	}

	public String createMessage() {
		return this.createMessage(this.book);
	}

	@SneakyThrows
	public String createMessage(@NonNull Book book) {

		var message = new StringBuilder();

		message.append("{");
		if( this.book.getId() != null ) {
			message.append(format("\"id\": %s",book.getId()));
		}
		message.append(format("\"name\":\"%s\",", book.getName()));
		message.append(format("\"description\":\"%s\",", book.getDescription()));

		message.append(format("\"author\":\"http://localhost:8080/api/authors/%s\",", book.getAuthor().getId()));
		message.append(format("\"genre\":\"http://localhost:8080/api/genres/%s\"", book.getGenre().getId()));
		message.append("}");

		return message.toString();
	}

}
