import { User } from './../../models/User.model';
import { UserService } from './../../services/user.service';
import { Component, OnInit } from '@angular/core';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';

interface Role {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'app-add-user-modal',
  templateUrl: './add-user-modal.component.html',
  styleUrls: ['./add-user-modal.component.css']
})
export class AddUserModalComponent implements OnInit {

  firstName = '';

  LastName = '';

  password: string;

  homePhone: number;

  cellPhone: number;

  workPhone: number;

  email: string;

  roles: Role[] = [
    {value: 'PATIENT', viewValue: 'Patient'},
    {value: 'RECEPTIONIST', viewValue: 'Receptionist'},
    {value: 'PRIMARY', viewValue: 'Primary'},
    {value: 'ONCOLOGIST', viewValue: 'Oncologist'},
    {value: 'ADMIN', viewValue: 'Admin'},
  ];

  selectedRole: string;

  constructor(private uService: UserService, public dialogRef: MatDialogRef<AddUserModalComponent>) { }

  ngOnInit(): void {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  saveNewUser() {
    const userName: string = this.firstName.substring(0, 1).toLowerCase() + this.LastName.toLowerCase();
    const user: User = {
      firstName: this.firstName,
      lastName: this.LastName,
      username: userName,
      password: this.password,
      homePhone: this.homePhone,
      cellPhone: this.cellPhone,
      workPhone: this.workPhone,
      email: this.email,
      role: [this.selectedRole]
    };
    console.log('User: ', user);
    this.uService.addUser(user).subscribe(result => {
      console.log('Result: ', result);
      this.dialogRef.close();
    });
  }

}
