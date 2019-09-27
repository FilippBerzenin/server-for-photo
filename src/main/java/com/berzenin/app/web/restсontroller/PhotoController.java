package com.berzenin.app.web.restсontroller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.annotation.security.PermitAll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.berzenin.app.model.Photo;
import com.berzenin.app.service.controller.MerchService;
import com.berzenin.app.service.controller.ObjectPlaceService;
import com.berzenin.app.service.controller.PhotoService;

@RestController
@RequestMapping(value="/api/photo")
public class PhotoController extends GenericControllerImpl<Photo, PhotoService> {
	
	@Autowired
	private ObjectPlaceService objectService;
	
	@Autowired
	private MerchService merchService;
	
	public PhotoController(PhotoService service) {
		super(service);
	}	
	
	@PermitAll
	@PostMapping(
			produces = "application/json"
			)
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(value="/uploadFile/")
	public Photo addPhotoWithParemetrs(
			@RequestParam("file") MultipartFile file,
			@RequestParam("merch_id") Long merch_Id,
			@RequestParam("object_id") Long object_id,
			@RequestParam("date") String date
			){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse(date, formatter);
		Photo photo = Photo.builder()
				.name(file.getOriginalFilename())
				.date(localDate)
				.objectPlace(objectService.findById(object_id))
				.merch(merchService.findById(merch_Id))
				.build();				
    	return service.add(photo, file).get();
	}
}
