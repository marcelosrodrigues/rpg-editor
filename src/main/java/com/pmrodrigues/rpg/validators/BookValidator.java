package com.pmrodrigues.rpg.validators;

import com.pmrodrigues.rpg.entities.BasicSecurityGroups;
import com.pmrodrigues.rpg.entities.Book;
import com.pmrodrigues.rpg.exceptions.NotPermittedException;
import com.pmrodrigues.rpg.services.security.helpers.AuthenticationHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RepositoryEventHandler(Book.class)
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class BookValidator {

    @Autowired
    private AuthenticationHelper authHelper;

    @HandleBeforeDelete
    public void onBeforeDelete(final Book book) {

        log.info("trying to delete a book {}" , book);

        if( !isOperationAccepted(book) ) {
            throw new NotPermittedException("Only administrators or the author of this book could delete them");
        }

    }

    @HandleBeforeSave
    public void onBeforeUpdate(final Book book) {
        log.info("trying to update a book {}" , book);

        if( !isOperationAccepted(book) ) {
            throw new NotPermittedException("Only administrators or the author of this book could update them");
        }

    }

    private boolean isOperationAccepted(final Book book) {
        var user = authHelper.getLoggedUser();
        return user.isInRole(BasicSecurityGroups.ADMIN) || book.getAuthor().equals(user);
    }
}
