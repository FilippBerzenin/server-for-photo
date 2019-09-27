package com.berzenin.app.model;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.engine.internal.CascadePoint;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"photos", "objectPlace"})
@ToString(exclude = {"photos", "objectPlace"})
public class Merch {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotNull
	@Size (min=1, max=100)
	private String name;
	
	@NotNull
	@Size (min=1, max=100)
	private String login;
	
	@NotNull
	@Size (min=1, max=100)
	private String pass;
	
	@JsonIgnore
	@OneToMany(mappedBy="merch", fetch = FetchType.LAZY)
	@Column(name = "users_photos")
	private List<Photo> photos;
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinTable(
			  name = "merch_object", 
			  joinColumns = @JoinColumn(name = "merch_id"), 
			  inverseJoinColumns = @JoinColumn(name = "object_id"))
	private Set<ObjectPlace> objectPlace;

}
