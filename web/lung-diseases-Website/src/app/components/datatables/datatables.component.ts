import { ModifyUserModalComponent } from './../modify-user-modal/modify-user-modal.component';
import { Component, OnInit, ElementRef, HostListener, AfterViewInit, ViewChild, ChangeDetectorRef, Input } from '@angular/core';
import { User } from 'src/app/models/User.model';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-datatables',
  templateUrl: './datatables.component.html',
  styleUrls: ['./datatables.component.css']
})
export class DatatablesComponent implements OnInit {
  @Input() user: User[];
  @Input() refresh: VoidFunction;
  paginationLength = 0;
  paginationPage = 1;
  filterByName: string;

  constructor(public dialog: MatDialog) { }

  ngOnInit(): void {
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(ModifyUserModalComponent, {
      width: '500px',
      data: {}
    });

    dialogRef.afterClosed().subscribe(result => {
      this.refresh();
    });
  }
}
