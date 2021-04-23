package test.com.pmrodrigues.rpg.validators;

import com.pmrodrigues.rpg.entities.Book;
import com.pmrodrigues.rpg.exceptions.NotPermittedException;
import com.pmrodrigues.rpg.services.security.helpers.AuthenticationHelper;
import com.pmrodrigues.rpg.validators.BookValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import test.com.pmrodrigues.rpg.factories.AuthorFactory;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookValidatorTest {

    @Mock
    private AuthenticationHelper authHelper;

    @InjectMocks
    private BookValidator validator;

    private AuthorFactory authorFactory = new AuthorFactory();

    @Test
    void shouldDelete() {

        var author = authorFactory
                    .buildNew()
                    .withID(randomUUID())
                    .getAuthor();

        when(authHelper.getLoggedUser()).thenReturn(author);

        var book = new Book();
        book.setAuthor(author);

        validator.onBeforeDelete(book);

    }

    @Test
    public void shouldUpdate() {

        var author = authorFactory
                .buildNew()
                .withID(randomUUID())
                .getAuthor();

        when(authHelper.getLoggedUser()).thenReturn(author);

        var book = new Book();
        book.setAuthor(author);

        validator.onBeforeUpdate(book);
    }

    @Test
    void shouldntDelete() {

        var logged = authorFactory
                .buildNew()
                .withID(randomUUID())
                .getAuthor();

        var book = new Book();

        when(authHelper.getLoggedUser()).thenReturn(logged);

        var author = authorFactory
                .buildNew()
                .withID(randomUUID())
                .getAuthor();

        book.setAuthor(author);
        assertThrows(NotPermittedException.class , () -> {
                validator.onBeforeDelete(book);
        });
    }

    @Test
    void shouldntUpdate() {

        var logged = authorFactory
                .buildNew()
                .withID(randomUUID())
                .getAuthor();

        var book = new Book();

        when(authHelper.getLoggedUser()).thenReturn(logged);

        var author = authorFactory
                .buildNew()
                .withID(randomUUID())
                .getAuthor();

        book.setAuthor(author);
        assertThrows(NotPermittedException.class , () -> {
            validator.onBeforeUpdate(book);
        });
    }
}