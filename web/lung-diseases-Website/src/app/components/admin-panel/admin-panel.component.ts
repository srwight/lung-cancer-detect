import { DatatablesComponent } from './../datatables/datatables.component';
import { UserService } from './../../services/user.service';
import { AddUserModalComponent } from './../add-user-modal/add-user-modal.component';
import { Component, OnInit } from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import { User } from 'src/app/models/User.model';

@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.css']
})
export class AdminPanelComponent implements OnInit {

  constructor(public dialog: MatDialog, private uService: UserService) { }

  users: User[];

  ngOnInit(): void {
    this.getUsers();
  }

  getUsers(): void {
    this.uService.getUser().subscribe(result => {
      console.log('Users: ', result);
      this.users = result;
    });
  }

  filterUserListByRole(role: string): User[] {
    return this.users.filter(u => u.role[0] === role);
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(AddUserModalComponent, {
      width: '500px',
      data: {}
    });

    dialogRef.afterClosed().subscribe(result => {
      this.getUsers();
    });
  }

}
