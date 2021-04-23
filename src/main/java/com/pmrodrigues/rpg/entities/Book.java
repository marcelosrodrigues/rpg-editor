package com.pmrodrigues.rpg.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.Persistable;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@JsonRootName(namespace = "com.pmrodrigues.rpg", value = "Book")
@JsonIgnoreProperties(ignoreUnknown = true)
@DynamicUpdate(true)
@Entity 
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RestResource
public class Book implements Persistable<UUID>{	

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	@EqualsAndHashCode.Include
	private UUID id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false , columnDefinition = "TEXT")
	private String description;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "author_id", referencedColumnName = "id")
	private Author author;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY )
	@JoinColumn(name = "genre_id", referencedColumnName = "id")
	private Genre genre;
	
	@Override
	@JsonIgnore
	public boolean isNew() {
		return this.id == null;
	}
	
	
}
