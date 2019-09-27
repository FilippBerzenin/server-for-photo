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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;

import com.berzenin.app.service.controller.ObjectPlaceService;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@EqualsAndHashCode(exclude = { "merch", "photos" })
@ToString(exclude = { "merch", "photos" })
public class ObjectPlace {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotNull
	@Size(min = 1, max = 100)
	private String name;

	@OneToMany(
			fetch = FetchType.LAZY, 
			mappedBy = "objectPlace", 
			cascade = CascadeType.ALL
			)
	@Column(name = "objects_photos")
	@JsonIgnore
	private List<Photo> photos;

	@ManyToMany(
			fetch = FetchType.LAZY, 
			mappedBy = "objectPlace", 
			cascade = CascadeType.ALL
			)
	@JsonIgnore
	private Set<Merch> merch;
}
