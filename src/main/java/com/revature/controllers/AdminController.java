package com.revature.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.revature.dao.models.UserDaoModel;
import com.revature.services.UserService;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AdminController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(method=RequestMethod.POST, path="/getuser", 
			produces=MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<UserDaoModel>> createUser() {
		return new ResponseEntity<List<UserDaoModel>>(userService.getAllUsers(), HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.POST, path="/adduser", 
			consumes=MediaType.APPLICATION_JSON_VALUE, 
			produces=MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<String> createUser(@RequestBody UserDaoModel user) {
		userService.addUser(user);
		return new ResponseEntity<String>("{\"result\":\"User was created\"}", HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.PUT, path="/modifyuser", 
			consumes=MediaType.APPLICATION_JSON_VALUE, 
			produces=MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<String> modifyUser(@RequestBody UserDaoModel user) {
		
		
		return new ResponseEntity<String>("", HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, path="/removeuser", 
			consumes=MediaType.APPLICATION_JSON_VALUE, 
			produces=MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<String> removeUser(@RequestBody UserDaoModel user) {
		return new ResponseEntity<String>("System has been successfully set up.", HttpStatus.OK);
	}
}
