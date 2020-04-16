import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable} from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class JwtService {

  constructor(private httpClient: HttpClient) { }

  // So what you have done? You first used the HttpClient.post method to send a request to /auth/login endpoint
  // with an object containing the email and password passed as parameters.
  // Next, you used the .pipe method which is a member of the RxJS Observable for chaining operators
  // and the tap function to execute a side effect for persisting the JWT access token,
  // returned by the server, in the browser's local storage.
  login(username: string, password: string) {
    return this.httpClient.post<{access_token: string}>('http://localhost:8888/login', {username, password}).pipe(tap(res => {
      localStorage.setItem('access_token', res.access_token);
    }));
  }

  // Again, you've used the HttpClient.post method to send a POST request to the server
  // with the registration information (email and password) then used the .pipe and tap function to run a side effect
  // that calls the .login method to logs the user in once the registration is done.
  register(username: string, password: string) {
    return this.httpClient.post<{access_token: string}>('http://localhost:8888/register', {username, password}).pipe(tap(res => {
      this.login(username, password);
    }));
  }

  logout() {
    localStorage.removeItem('access_token');
  }

  public get loggedIn(): boolean {
    return localStorage.getItem('access_token') !==  null;
  }
}
