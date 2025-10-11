import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Need } from '../need';

@Injectable({
  providedIn: 'root'
})
export class NeedsCupboardService {
  private needsUrl = 'http://localhost:8080/needs';
  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json'})
  };

  constructor(private http: HttpClient) {}

  /**
   * Fetches all needs from the backend
   * @returns An observable array of Need objects
   */
  getNeeds(): Observable<Need[]> {
    return this.http.get<Need[]>(this.needsUrl).pipe(
      tap(() => console.log('Fetched needs')),
      catchError(this.handleError<Need[]>('getNeeds', []))
    );
  }

  /**
   * Handle the HTTP errors for all requests
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = 'operation', result?: T){
    return (error: any): Observable<T> => {
      console.error(`${operation} failed: ${error.message}`);
      return of(result as T);
    }
  }
}
