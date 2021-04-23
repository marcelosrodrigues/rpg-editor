package com.pmrodrigues.rpg.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonRootName;


@Data
@NoArgsConstructor
@RequiredArgsConstructor
@JsonRootName(namespace = "com.pmrodrigues.rpg", value = "Rule")
@JsonInclude(content = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true, value = {"hibernateLazyInitializer", "handler"})
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Rule implements GrantedAuthority {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	@EqualsAndHashCode.Include
	private UUID id;
	
	@Column(nullable = false , unique = true)
	@NonNull
	private String authority;

}
