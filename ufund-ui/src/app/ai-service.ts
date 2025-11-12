import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { APIResponse } from './response';

@Injectable({
  providedIn: 'root'
})
export class AiService {
  
  private aiUrl = "http://localhost:8080/responses"

  constructor(private http: HttpClient){}

  // HTTP options for POST and PUT requests
  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json' })
  };
  
  // creates a response from the GeminiAPI with the given prompt 
  createResponse(prompt: String) : Observable<APIResponse> {

    return this.http.post<APIResponse>(this.aiUrl, prompt, this.httpOptions);

  }

}
