package test.com.pmrodrigues.rpg.converters;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pmrodrigues.rpg.converters.AuthorConverter;
import com.pmrodrigues.rpg.entities.Author;
import com.pmrodrigues.rpg.repositories.AuthorRepository;

import lombok.SneakyThrows;

@ExtendWith(MockitoExtension.class)
public class AuthorConverterTest {

	@Mock
	private AuthorRepository repository;

	@InjectMocks
	private AuthorConverter toTest;

	@SneakyThrows
	@Test
	public void shouldFound() {

		when(repository.findById((UUID) notNull())).thenReturn(Optional.of(new Author()));

		Assertions.assertNotNull(toTest.convert(new URI(UUID.randomUUID().toString())));
	}

	@SneakyThrows
	@Test
	public void shouldNotFound() {
		when(repository.findById((UUID) notNull())).thenReturn(Optional.empty());

		Assertions.assertNull(toTest.convert(new URI(UUID.randomUUID().toString())));
	}

}
