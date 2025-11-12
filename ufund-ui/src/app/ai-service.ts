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
  
  createResponse(prompt: String) : Observable<String> {

    return this.http.post<String>(this.aiUrl, prompt, this.httpOptions);

  }

  getResponse(prompt: String) :Observable<String> {
    return this.http.get<String>(`${this.aiUrl}/${prompt}`);
  }

}
