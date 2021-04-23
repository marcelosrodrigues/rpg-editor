package com.pmrodrigues.rpg.repositories;

import com.pmrodrigues.rpg.entities.Author;
import com.pmrodrigues.rpg.entities.Book;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookRepository extends PagingAndSortingRepository<Book, UUID> {

	@Query(value = "SELECT b FROM Book b WHERE b.author = :author")
	Page<Book> findByAuthor(@Param("author") Author author, Pageable page);

	@Query(value = "SELECT b FROM Book b WHERE b.genre.path LIKE :path%")
	Page<Book> findByGenre(@Param("path") String path, Pageable page);

	@PreAuthorize("hasAnyAuthority('Administrators','Authors')")
	@Override
	Book save(@NonNull final Book book);

	@PreAuthorize("hasAnyAuthority('Administrators','Authors')")
	@Override
	void delete(@NonNull final Book book);

	@PreAuthorize("hasAnyAuthority('Administrators','Authors')")
	@Override
	void deleteAll();

}
