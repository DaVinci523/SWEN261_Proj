import { Injectable } from '@angular/core';
import { Need } from './need';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {MessageService} from './message';


@Injectable({
  providedIn: 'root'
})
export class AdminPageService {

  private needsUrl = 'http://localhost:8080/needs';

  constructor(private http: HttpClient,  private MessageService: MessageService) {}

  private log(message: string) {
    this.MessageService.add(`NeedService: ${message}`);
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   *
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  getNeeds(): Observable<Need[]> {
    return this.http.get<Need[]>(this.needsUrl);
  }

  updateNeed(need:any, name: string, value: number, quantity: number, type: string):Observable<any>{
    const updatedNeed: Need = {id: need.id, name, value, quantity, type};
    
    return this.http.put<Need>(this.needsUrl, updatedNeed).pipe(
      tap(_ => this.log(`updated need id=${need.id}`)),
      catchError(this.handleError<any>('updateNeed'))
    );
  } 

  httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };


  createNeed(need: any): Observable<any> {
       return this.http.post<Need>(this.needsUrl, need, this.httpOptions).pipe(
        tap((newNeed: Need) => this.log(`added need w/ id=${newNeed.id}`)),
        catchError(this.handleError<Need>('addNeed'))
      );
  }

  deleteNeedID(needId: number): Observable<any> {
      return this.http.delete<Need>(`${this.needsUrl}/${needId}`).pipe(
        tap(_ => this.log(`deleted need id=${needId}`)),
      catchError(this.handleError<Need>('deleteNeed'))
    );
  }

}
