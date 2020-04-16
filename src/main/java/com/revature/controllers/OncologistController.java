package com.revature.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.revature.models.Nodule;
import com.revature.models.PatientScan;
import com.revature.services.AIService;

@RestController
@RequestMapping("api/oncologist/tensorflow")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OncologistController {

	@Autowired
	private AIService aiService;
	
	@RequestMapping(method=RequestMethod.GET, path="/test", 
			produces=MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('ROLE_PATIENT','ROLE_ONCOLOGIST')")
	public ResponseEntity<String> testPrediction() {
		
		return new ResponseEntity<String>(aiService.getPredictedValue(), HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.GET, path="/results", 
			produces=MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('ROLE_PATIENT','ROLE_ONCOLOGIST')")
	public ResponseEntity<PatientScan> showPatientScan() {
		PatientScan scan = new PatientScan();
		scan.setNodule(new Nodule());
		scan.getNodule().setX(125);
		scan.getNodule().setY(512);
		scan.getNodule().setZ(512);
		scan.setConfidence(63);
		return new ResponseEntity<PatientScan>(scan, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.POST, path="/results", 
			produces=MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ONCOLOGIST')")
	public ResponseEntity<PatientScan> getPatientScan() {
		PatientScan scan = new PatientScan();
		scan.setNodule(new Nodule());
		scan.getNodule().setX(125);
		scan.getNodule().setY(512);
		scan.getNodule().setZ(512);
		scan.setConfidence(63);
		return new ResponseEntity<PatientScan>(scan, HttpStatus.OK);
	}
}
