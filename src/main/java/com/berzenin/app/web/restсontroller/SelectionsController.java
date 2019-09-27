package com.berzenin.app.web.restсontroller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.berzenin.app.model.Photo;
import com.berzenin.app.service.controller.SelectionsService;

@RestController
@RequestMapping(value="/api/timetable")
public class SelectionsController extends GenericControllerImpl<Photo, SelectionsService>  {

	public SelectionsController(SelectionsService service) {
		super(service);
	}	

}
