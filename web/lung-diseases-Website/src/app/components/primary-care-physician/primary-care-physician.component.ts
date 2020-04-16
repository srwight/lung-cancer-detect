import { PrimaryPhysicianServiceService } from './../../services/primary-physician-service.service';
import { TenserFlow } from './../../models/tenserflow.model';
import { Component, OnInit } from '@angular/core';
import { TicksDataModel } from '@syncfusion/ej2-angular-inputs';
import {NgbCarouselConfig} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-primary-care-physician',
  templateUrl: './primary-care-physician.component.html',
  styleUrls: ['./primary-care-physician.component.css'],
  providers: [NgbCarouselConfig]
})
export class PrimaryCarePhysicianComponent implements OnInit {

  constructor(config: NgbCarouselConfig, private primaryService: PrimaryPhysicianServiceService) {
    config.showNavigationArrows = true;
    config.showNavigationIndicators = true;
   }

  showNavigationArrows = false;
  showNavigationIndicators = false;

  framesPerSecond: number;
  imageIndex: number;
  imageSrc: string;
  pictures: string[] = [];
  results: TenserFlow;

  ticks: TicksDataModel = {
    placement: 'Both', largeStep: 200,
    showSmallTicks: true, smallStep: 100
  };

  ngOnInit() {
    this.getData();
    this.framesPerSecond = 500;
    this.imageIndex = 0;
    for (let index = 0; index < 143; index++) {
      this.pictures.push('scan_image_' + index);
    }
    // this.CycleImage();
  }

  getData() {
    this.primaryService.getTest().subscribe(
      resp => {
        if (resp != null) {
          console.log(resp);
          this.results = resp;
        } else {
          console.error('Error loading Users. Null value loaded');
        }
      }
    );

  }

  // CycleImage() {
  //   setTimeout(() => {
  //     this.imageSrc = 'scan_image_' + this.imageIndex;
  //     if (this.imageIndex < 142) {
  //       this.imageIndex = this.imageIndex + 1;
  //     } else {
  //       this.imageIndex = 0;
  //     }
  //     this.CycleImage();
  //   }, 1000 - this.framesPerSecond);
  // }
}
