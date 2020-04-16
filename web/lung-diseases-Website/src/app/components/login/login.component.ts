import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../services/authentication.service';
import * as jwt_decode from 'jwt-decode';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  username = '';
  password = '';
  invalidLogin = false;

  constructor(private loginservice: AuthenticationService) { }

  ngOnInit(): void {
  }

  checkLogin() {
    this.loginservice.authenticate(this.username, this.password).subscribe(
      userData => {
        userData.headers.keys();
        if (userData.headers.get('authorization')) {
          sessionStorage.setItem('username', this.username);
          const tokenStr = userData.headers.get('authorization');
          sessionStorage.setItem('token', tokenStr);
          const decoded = jwt_decode(tokenStr);
          const role = decoded.authorities.findIndex((auth) => auth.authority.startsWith('ROLE_'), 'ROLE_');
          this.loginservice.switchAppView(decoded.authorities[role].authority);
          this.invalidLogin = false;
        } else {
          this.invalidLogin = true;
        }
      }
    );
  }
}
