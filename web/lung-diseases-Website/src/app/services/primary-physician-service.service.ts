import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TenserFlow } from '../models/tenserflow.model';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
  })
};

@Injectable({
  providedIn: 'root'
})
export class PrimaryPhysicianServiceService {

  url = `http://localhost:8888`;

  constructor(private http: HttpClient) { }

  public getTest(): Observable<TenserFlow> {
    return this.http.post<TenserFlow>(`${this.url}/api/primary/tensorflow/results`, httpOptions);
  }
}
