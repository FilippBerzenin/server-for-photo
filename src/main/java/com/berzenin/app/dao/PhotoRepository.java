package com.berzenin.app.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.berzenin.app.model.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
	
	public List<Photo> findByMerch_nameAndDate(String name, LocalDate date);

	Optional<Photo> findByName(String name);
}
