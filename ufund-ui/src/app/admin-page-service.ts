import { Injectable } from '@angular/core';
import { Need } from './need';
import { Observable, of } from 'rxjs';

import { HttpClient, HttpHeaders } from '@angular/common/http';



@Injectable({
  providedIn: 'root'
})
export class AdminPageService {

  private needsUrl = 'http://localhost:8080/needs';

  constructor(private http: HttpClient) {}

  getNeeds(): Observable<Need[]> {
     
    return this.http.get<Need[]>(this.needsUrl);

  }

  updateNeed(need:any ):Observable<any>{
    return this.http.put<any>(this.needsUrl, need);
  } 

  createNeed(need: any): Observable<any> {
      const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
      return this.http.post<any>(this.needsUrl, need, { headers });
  }

  deleteNeedID(needId: number): Observable<any> {
      return this.http.delete<any>(`${this.needsUrl}/${needId}`);
  }

}
