import { UserService } from './services/user.service';
import { PrimaryPhysicianServiceService } from './services/primary-physician-service.service';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './components/home/home.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { PrimaryCarePhysicianComponent } from './components/primary-care-physician/primary-care-physician.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SliderModule } from '@syncfusion/ej2-angular-inputs';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { JwtModule } from '@auth0/angular-jwt';

import { TensorflowService } from './services/tensorflow.service';
import { JwtService } from './services/jwt.service';
import { FooterComponent } from './components/footer/footer/footer.component';
import { LoginComponent } from './components/login/login.component';
import { LogoutComponent } from './components/logout/logout.component';
import { BasicAuthInterceptorService } from './services/basic-auth-interceptor.service';
import { PatientRegisterComponent } from './components/patient-register/patient-register.component';
import { AdminPanelComponent } from './components/admin-panel/admin-panel.component';
import { ReceptionistPageComponent } from './components/receptionist-page/receptionist-page.component';
import { OncologistPageComponent } from './components/oncologist-page/oncologist-page.component';
import { PatientPageComponent } from './components/patient-page/patient-page.component';
import { AddUserModalComponent } from './components/add-user-modal/add-user-modal.component';
import { IgxTabsModule } from 'igniteui-angular';
import { MatSelectModule } from '@angular/material/select';
import {MatDialogModule, MatDialogRef} from '@angular/material/dialog';
import { DatatablesComponent } from './components/datatables/datatables.component';
import { DataTablesModule } from 'angular-datatables';
import { FilterByNamePipe } from './pipes/filter-by-name.pipe';
import { ModifyUserModalComponent } from './components/modify-user-modal/modify-user-modal.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    HomeComponent,
    PrimaryCarePhysicianComponent,
    FooterComponent,
    LoginComponent,
    LogoutComponent,
    PatientRegisterComponent,
    AdminPanelComponent,
    ReceptionistPageComponent,
    OncologistPageComponent,
    PatientPageComponent,
    AddUserModalComponent,
    DatatablesComponent,
    FilterByNamePipe,
    ModifyUserModalComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    SliderModule,
    NgbModule,
    HttpClientModule,
    FormsModule,
    IgxTabsModule,
    MatSelectModule,
    MatDialogModule,
    DataTablesModule,
    JwtModule.forRoot({
      config: {
        tokenGetter: function tokenGetter() {
          return localStorage.getItem('access_token');
        },
        whitelistedDomains: ['localhost:8888'],
        blacklistedRoutes: ['http://localhost:8888/login']
      }
    })
  ],
  exports: [
    MatSelectModule,
    MatDialogModule,
  ],
  providers: [
    TensorflowService,
    PrimaryPhysicianServiceService,
    UserService,
    AddUserModalComponent,
    ModifyUserModalComponent,
    {
      provide: HTTP_INTERCEPTORS, useClass: BasicAuthInterceptorService, multi: true
    },
    {
      provide: MatDialogRef,
      useValue: {}
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
