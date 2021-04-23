package test.com.pmrodrigues.rpg.factories;

import com.pmrodrigues.rpg.entities.Genre;
import com.pmrodrigues.rpg.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Arrays.asList;

@Component
public class GenreFactory {

	@Autowired
	private GenreRepository genreRepository;

	private final List<String> GENRES = asList("Action",
			"Adventure",
			"Animation",
			"Comedy",
			"Crime",
			"Drama",
			"Family",
			"Fantasy",
			"History",
			"Horror",
			"Music",
			"Mystery",
			"Romance",
			"Science Fiction",
			"Thriller",
			"War",
			"Western");
	
	private Genre genre;

	public List<Genre> createNewList( ) {

		var categories = new ArrayList<Genre>();

		GENRES.forEach(category -> {
			categories.add( new Genre(category) );
		});

		var saved = this.genreRepository.saveAll(categories);
		return StreamSupport.stream(saved.spliterator(), false)
			.collect(Collectors.toList());
	}

	public GenreFactory withName(String name) {
		if( this.genre == null ) {
			this.buildNew();
		}
		this.genre.setName(name);
		return this;
	}

	public GenreFactory buildNew() {
		this.genre = new Genre();
		return this;
	}

	public Genre getCategory() {
		return this.genre;
	}


	public GenreFactory withSubGenres() {
		this.genre.addSubGenres(this.createNewList());
		return this;
	}

	public GenreFactory save() {
		this.genreRepository.save(this.genre);
		return this;
	}
}
