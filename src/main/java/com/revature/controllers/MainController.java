package com.revature.controllers;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.services.TensorflowService;

@RestController
public class MainController {

	@Autowired
	TensorflowService service;
	
	@RequestMapping("/")
	public String test() {
		
		try {
			return "service is working " + service.Check();
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		
		return "service is working not no data is avalable";
	}

}
