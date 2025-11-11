import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AiService {
  
  private aiUrl = "http://localhost:8080/responses"

  constructor(private http: HttpClient){}

  // HTTP options for POST and PUT requests
  httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };
  
  getResponse(prompt:String) : Observable<any> {

    return this.http.post(this.aiUrl, prompt, this.httpOptions);

  }

}
