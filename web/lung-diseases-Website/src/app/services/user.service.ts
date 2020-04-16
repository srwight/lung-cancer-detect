import { Injectable } from '@angular/core';
import {User} from '../models/User.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable()
export class UserService {

  url = `http://localhost:8888`;
  constructor(private http: HttpClient) {

  }
  public getUser() {
    return this.http.post<User[]>(`${this.url}/api/admin/getuser`, {});
  }

  public getByUsername(username: string) {
    return this.http.get<User[]>(`${this.url}/users/?username=${username}`);
  }

  public addUser(user: User) {
    return this.http.post(`${this.url}/api/admin/adduser`, user, httpOptions);
  }
}
