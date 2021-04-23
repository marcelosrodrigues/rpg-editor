package test.com.pmrodrigues.rpg.factories;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import com.pmrodrigues.rpg.entities.Rule;
import com.pmrodrigues.rpg.entities.User;

import com.pmrodrigues.rpg.repositories.UserRepository;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UserFactory {

	@Getter
	private User user;

	@Autowired
	private UserRepository repository;

	public UserFactory save() {
		if( this.user != null ){
			this.repository.save(this.user);
		}
		return this;
	}

	public UserFactory buildNew() {
		user = new User();
		user.setName("TESTE");
		user.setBirthDate(Date.from(Instant.now()));
		user.setEmail("should-insert@teste.com");

		return this;
	}
	
	public UserFactory withName(@NonNull String name) {
		this.user.setName(name);
		return this;
	}
	
	public UserFactory withBirthDay(@NonNull Date birthDay) {
		this.user.setBirthDate(birthDay);
		return this;
	}
	
	public UserFactory withEmail(@NonNull String email) {
		this.user.setEmail(email);
		return this;
	}
	
	public UserFactory withExpiredAccountDate(@NonNull Date expiredDate) {
		this.user.setExpiredAccountDate(expiredDate);
		return this;
	}

	public UserFactory withExpiredCredentialDate(@NonNull Date expiredDate) {
		this.user.setExpiredCredentialDate(expiredDate);
		return this;
	}


    public UserFactory withRole(String roleName) {
		var rule = new Rule();
		rule.setAuthority(roleName);
		this.user.addAuthority(rule);
		return this;
    }

	public UserFactory withRole(Rule rule) {
		this.user.addAuthority(rule);
		return this;
	}

    public UserFactory withID( UUID id ){
		this.user.setId(id);
		return this;
	}
}
