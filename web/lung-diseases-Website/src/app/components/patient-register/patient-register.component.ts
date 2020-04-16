import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-patient-register',
  templateUrl: './patient-register.component.html',
  styleUrls: ['./patient-register.component.css']
})
export class PatientRegisterComponent implements OnInit {

  firstName;

  LastName;

  homePhone;

  cellPhone;

  workPhone;

  constructor() { }

  ngOnInit(): void {
  }

  checkPatientIn() {

  }

}
