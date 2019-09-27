package com.berzenin.app.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.berzenin.app.model.Merch;

@Repository
public interface MerchRepository extends JpaRepository<Merch, Long> {
	
	Optional<Merch> findByLoginAndPass(String login, String pass);
	
	Optional<Merch> findByName(String name);

}
