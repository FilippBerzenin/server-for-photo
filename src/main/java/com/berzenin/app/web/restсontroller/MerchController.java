package com.berzenin.app.web.restсontroller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.security.PermitAll;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.berzenin.app.model.Merch;
import com.berzenin.app.service.controller.MerchService;

import lombok.Data;

@RestController
@RequestMapping(value = "/api/merch")
public class MerchController extends GenericControllerImpl<Merch, MerchService> {

	public MerchController(MerchService service) {
		super(service);
	}

	@GetMapping(
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, 
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.ACCEPTED)
	@RequestMapping(value = "/authentication")
	public boolean authorizationMerch(
			@RequestParam("login") String login,
			@RequestParam("pass") String pass) {
		if (service.getMercByLoginAndPass(login, pass).isPresent()) {
			return true;
		} else return false;
	}
	
	@PermitAll
	@GetMapping(
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, 
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.ACCEPTED)
	@RequestMapping(value = "/get")
	public Merch getMerch(
			@RequestParam("login") String login,
			@RequestParam("pass") String pass) {
		Optional<Merch> merch = service.getMercByLoginAndPass(login, pass);
		if (merch.isPresent()) {
			return merch.get();
		} else return merch.get(); 
	}
	
	@PermitAll
	@GetMapping(
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, 
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.ACCEPTED)
	@RequestMapping(value = "/shops")
	public List<Shop> getShopsFromMerch(
			@RequestParam("id") long id_merch) {
		List<Shop> shops = service.getShopsFromMerch(id_merch).get().stream()
				.map(object -> 
					new Shop(object.getId(), object.getName())
				).collect(Collectors.toList());
		return shops;
	}
	
	@Data
	private class Shop {
		
		private long id;
		private String name;
		
		public Shop(long id, String name) {
			this.id = id;
			this.name = name;
		}		
	}
}
