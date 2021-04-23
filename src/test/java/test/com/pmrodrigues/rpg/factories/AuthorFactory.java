package test.com.pmrodrigues.rpg.factories;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import com.pmrodrigues.rpg.entities.Author;

import com.pmrodrigues.rpg.entities.Rule;
import com.pmrodrigues.rpg.repositories.AuthorRepository;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthorFactory {

	@Getter
	private Author author;

	@Autowired
	private AuthorRepository repository;

	public AuthorFactory buildNew() {
		author = new Author();
		author.setName("TESTE");
		author.setBirthDate(Date.from(Instant.now()));
		author.setEmail("should-insert@teste.com");

		return this;
	}
	
	public AuthorFactory withName(@NonNull String name) {
		this.author.setName(name);
		return this;
	}
	
	public AuthorFactory withBirthDay(@NonNull Date birthDay) {
		this.author.setBirthDate(birthDay);
		return this;
	}
	
	public AuthorFactory withEmail(@NonNull String email) {
		this.author.setEmail(email);
		return this;
	}

	public AuthorFactory withID( UUID id ){
		this.author.setId(id);
		return this;
	}

	public AuthorFactory withExpiredAccountDate(@NonNull Date expiredDate) {
		this.author.setExpiredAccountDate(expiredDate);
		return this;
	}

	public AuthorFactory withExpiredCredentialDate(@NonNull Date expiredDate) {
		this.author.setExpiredCredentialDate(expiredDate);
		return this;
	}

	public AuthorFactory save() {
		if( this.author != null) {
			var existed = repository.findByEmail(this.author.getEmail());
			if (existed == null)
				this.repository.save(this.author);
			else this.author = existed;
		}
		return this;
	}


    public AuthorFactory withRule(Rule rule) {
		this.author.getAuthorities()
				   .removeIf(auth -> auth.getAuthority().equalsIgnoreCase(rule.getAuthority()));

		this.author.addAuthority(rule);

		return this;
    }
}
