import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';

export class User {
  constructor(
    public status: string,
     ) {}
}

export class JwtResponse {
  constructor(
    public jwttoken: string,
     ) {}
}

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  helper = new JwtHelperService();

  constructor(private httpClient: HttpClient, private router: Router) {}

  authenticate(username, password) {
    return this.httpClient.post<any>('http://localhost:8888/login', {username, password}, {observe: 'response'});
    // .pipe(
    //   map(
    //     userData => {
    //       userData.headers.keys();
    //       console.log('login: ', userData.headers.get('authorization'));
    //       sessionStorage.setItem('username', username);
    //       const tokenStr = userData.headers.get('authorization');
    //       sessionStorage.setItem('token', tokenStr);
    //       return userData;
    //     }
    //   )
    // );
  }

  switchAppView(role: string) {
    console.log('Role: ', role);
    if (role.trim() === 'ROLE_ADMIN') {
      this.router.navigate(['admin-panel']);
    } else if (role.trim() === 'ROLE_ONCOLOGIST') {
      this.router.navigate(['oncologist-page']);
    } else if (role.trim() === 'ROLE_PRIMARY') {
      this.router.navigate(['primary-care-physician']);
    } else if (role.trim() === 'ROLE_RECEPTIONIST') {
      this.router.navigate(['receptionist-page']);
    } else if (role.trim() === 'ROLE_PATIENT') {
      this.router.navigate(['patient-page']);
    }
  }

  isUserLoggedIn() {
    const user = sessionStorage.getItem('username');
    return !(user === null || this.helper.isTokenExpired(sessionStorage.getItem('token')));
  }

  logOut() {
    sessionStorage.removeItem('username');
    sessionStorage.removeItem('token');
  }
}
