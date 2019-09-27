package com.berzenin.app.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.berzenin.app.model.ObjectPlace;

@Repository
public interface ObjectPlaceRepository extends JpaRepository<ObjectPlace, Long> {

	Optional<ObjectPlace> findByName(String name);

}
