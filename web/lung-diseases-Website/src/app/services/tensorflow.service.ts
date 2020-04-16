import { TenserFlow } from './../models/tenserflow.model';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable} from 'rxjs';


const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
  })
};

@Injectable({
  providedIn: 'root'
})
export class TensorflowService {

  url = `http://localhost:8888`;

  constructor(private http: HttpClient) { }

  public getTest(): Observable<TenserFlow> {
    return this.http.get<TenserFlow>(`${this.url}/api/oncologist/tensorflow/results`, httpOptions);
  }
}
