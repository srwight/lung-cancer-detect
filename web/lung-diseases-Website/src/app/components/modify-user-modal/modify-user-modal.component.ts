import { Component, OnInit, Input } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { UserService } from 'src/app/services/user.service';
import { User } from 'src/app/models/User.model';

interface Role {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'app-modify-user-modal',
  templateUrl: './modify-user-modal.component.html',
  styleUrls: ['./modify-user-modal.component.css']
})
export class ModifyUserModalComponent implements OnInit {

  @Input() user: User;

  roles: Role[] = [
    {value: 'PATIENT', viewValue: 'Patient'},
    {value: 'RECEPTIONIST', viewValue: 'Receptionist'},
    {value: 'PRIMARY', viewValue: 'Primary'},
    {value: 'ONCOLOGIST', viewValue: 'Oncologist'},
    {value: 'ADMIN', viewValue: 'Admin'},
  ];

  homePhone: number;

  cellPhone: number;

  workPhone: number;

  email: string;

  constructor(private uService: UserService, public dialogRef: MatDialogRef<ModifyUserModalComponent>) { }

  ngOnInit(): void {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  saveUserDetails() {
    console.log('User: ', this.user);
    this.uService.addUser(this.user).subscribe(result => {
      console.log('Result: ', result);
      this.dialogRef.close();
    });
  }
}
