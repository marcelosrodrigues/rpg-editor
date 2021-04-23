package com.pmrodrigues.rpg.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.Persistable;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@JsonRootName(namespace = "com.pmrodrigues.rpg", value = "Genre")
@JsonInclude(content = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true, value = { "hibernateLazyInitializer", "handler" })
@DynamicUpdate(true)
@Entity
@RestResource
@Slf4j
public class Genre implements Persistable<UUID> {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	@EqualsAndHashCode.Include
	private UUID id;
	
	@Column
	@Setter(AccessLevel.NONE)
	private String path;

	@Column(nullable = false)
	@NonNull
	private String name;

	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "parent_id", referencedColumnName = "id", nullable = true)
	@Setter(value = AccessLevel.NONE)
	@ToString.Exclude
	private Collection<Genre> subGenres = new HashSet<Genre>();
	
	@ManyToOne(fetch = FetchType.LAZY ,optional = true)
	@JoinColumn(name = "parent_id", referencedColumnName = "id", nullable = true)
	private Genre parent;

	@JsonIgnore
	@Override
	public boolean isNew() {
		return id == null;
	}

	public void addSubGenres(@NonNull final Collection<Genre> subGenres) {
		subGenres.forEach(
				category -> {
					category.updatePath(this.path);
					category.setParent(this);
				}

		);
		this.subGenres.addAll(subGenres);
	
	}
	
	@PrePersist
	@PreUpdate
	public void generatePath() {

		log.info("start to generate path to genre {}", this.name);

		if( this.parent == null ) {
			this.path = "1";
		} 
		
		long position = 1L;
		for( Genre genre : this.subGenres) {
			genre.updatePath(this.path + "." + position);
			if( log.isDebugEnabled() ) {
				log.debug("path created to {} - {}", genre , genre.getPath());
			}

			position++;
		}

		log.info("generation of path to genre {} was completed", this.name);
	}

	private void updatePath( final String path ) {
		this.path = path;

		long position = 1L;
		for( Genre genre : this.subGenres) {
			genre.updatePath(this.path + "." + position);
			if( log.isDebugEnabled() ) {
				log.debug("path created to {} - {}", genre , genre.getPath());
			}

			position++;
		}
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == null || !(obj instanceof Genre))
			return false;
		var other = (Genre) obj;
		return (other.id != null && this.id != null && this.id.equals(other.id));
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder().append(this.id).toHashCode();
	}

}
