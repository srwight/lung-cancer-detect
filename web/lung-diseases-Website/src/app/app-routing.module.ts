import { OncologistPageComponent } from './components/oncologist-page/oncologist-page.component';
import { AdminPanelComponent } from './components/admin-panel/admin-panel.component';
import { ReceptionistPageComponent } from './components/receptionist-page/receptionist-page.component';
import { PatientRegisterComponent } from './components/patient-register/patient-register.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { PrimaryCarePhysicianComponent } from './components/primary-care-physician/primary-care-physician.component';
import { LoginComponent } from './components/login/login.component';
import { LogoutComponent } from './components/logout/logout.component';
import { AuthGaurdService } from './services/auth-gaurd.service';
import { PatientPageComponent } from './components/patient-page/patient-page.component';

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full'},
  { path: 'home', component: HomeComponent},
  { path: 'primary-care-physician', component: PrimaryCarePhysicianComponent, canActivate: [AuthGaurdService]},
  { path: 'patient-register', component: PatientRegisterComponent, canActivate: [AuthGaurdService]},
  { path: 'receptionist-page', component: ReceptionistPageComponent, canActivate: [AuthGaurdService]},
  { path: 'oncologist-page', component: OncologistPageComponent, canActivate: [AuthGaurdService]},
  { path: 'patient-page', component: PatientPageComponent, canActivate: [AuthGaurdService]},
  { path: 'admin-panel', component: AdminPanelComponent, canActivate: [AuthGaurdService]},
  { path: 'login', component: LoginComponent },
  { path: 'logout', component: LogoutComponent, canActivate: [AuthGaurdService] },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
