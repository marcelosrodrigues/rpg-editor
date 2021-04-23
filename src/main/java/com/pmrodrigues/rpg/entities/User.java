package com.pmrodrigues.rpg.entities;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

import javax.persistence.*;
import javax.validation.constraints.Email;

import lombok.*;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.Persistable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonRootName;

@Data
@NoArgsConstructor
@JsonRootName(namespace = "com.pmrodrigues.rpg", value = "User")
@JsonInclude(content = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true, value = {"hibernateLazyInitializer", "handler"})
@DynamicUpdate(true)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.CHAR, length = 1)
@DiscriminatorValue("U")
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User implements Persistable<UUID>, UserDetails{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	@EqualsAndHashCode.Include
	private UUID id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false , unique = true)
	@Email
	private String email;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date birthDate;
	
	@Column(nullable = false)
	@ToString.Exclude
	@JsonIgnore
	@Setter(AccessLevel.NONE)
	private String password;
	
	@Transient
	@ToString.Exclude
	@Setter(AccessLevel.NONE)
	@JsonIgnore
	private String cleanPassword;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	@JsonIgnore
	private Date expiredAccountDate;

	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	@JsonIgnore
	private Date expiredCredentialDate;
	

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_authority_by_rules",
		joinColumns = {@JoinColumn( nullable = false, name = "user_id" , referencedColumnName = "id" )},
		inverseJoinColumns = {@JoinColumn( nullable = false, name = "rule_id" , referencedColumnName = "id") },
		uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "rule_id"}) )
	private Collection<Rule> authorities = new HashSet<>();
	
	
	@PrePersist
	public void hashPassword() {
		
		final PasswordEncoder ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		
		cleanPassword = randomAlphabetic(12);
		this.password = ENCODER.encode(cleanPassword);
		
		if( this.expiredCredentialDate == null ) {
			this.expiredCredentialDate = Date.from(Instant.now().minus(1, ChronoUnit.DAYS));
		}
		
		if( this.expiredAccountDate == null ) {
			expiredAccountDate = Date.from(Instant.now().plus(90, ChronoUnit.DAYS));
		}
		
	}

	@Override
	@JsonIgnore
	public boolean isNew() {
		return this.id == null;
	}

	@Override
	@JsonIgnore
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.getEmail();
	}
	
	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return Date.from(Instant.now()).before(this.expiredAccountDate);
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return Date.from(Instant.now()).before(this.expiredCredentialDate);
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

    public boolean isInRole(@NonNull String role) {
		var founded = this.authorities
					.stream()
					.filter(auth -> auth.getAuthority().equalsIgnoreCase(role))
					.findFirst();
		return founded.isPresent();
    }

	public void addAuthority(final Rule rule) {
		if( !this.authorities.contains(rule) ) {
			this.authorities.add(rule);
		}
	}
}
