package test.com.pmrodrigues.rpg.entities;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.junit.jupiter.api.Test;

import test.com.pmrodrigues.rpg.factories.UserFactory;

public class TestUser {

	@Test
	public void shouldNotAccountExpired() {
	
		var user = new UserFactory()
					.buildNew()
					.withExpiredAccountDate(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
					.getUser();
		assertTrue(user.isAccountNonExpired());
		
	}
	
	@Test
	public void shouldAccountExpired() {
		
		var user = new UserFactory()
							  .buildNew()
							  .withExpiredAccountDate(Date.from(Instant.now().minus(1, ChronoUnit.DAYS)))
							  .getUser();
		assertFalse(user.isAccountNonExpired());
	}
	
	@Test
	public void shouldChangePassword() {
		
		var user = new UserFactory()
				  .buildNew()
				  .withExpiredCredentialDate(Date.from(Instant.now().minus(1, ChronoUnit.DAYS)))
				  .getUser();
		assertFalse(user.isCredentialsNonExpired());
		
	}
	
	@Test
	public void shouldNotChangePassword() {
		
		var user = new UserFactory()
				  .buildNew()
				  .withExpiredCredentialDate(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
				  .getUser();
		assertTrue(user.isCredentialsNonExpired());
		
	}
	
	
}
