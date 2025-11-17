import { Injectable } from '@angular/core';
import { Need } from './need';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {MessageService} from './message';
import { NeedsCupboardService } from './needs-cupboard-service';


@Injectable({
  providedIn: 'root'
})
export class AdminPageService {

  private needsUrl = 'http://localhost:8080/needs';

  constructor(private http: HttpClient,  private MessageService: MessageService, private needsCupboardService: NeedsCupboardService) {}

  /** 
   * Log a NeedService message with the MessageService 
   *
   * @param message - message to be sent 
   */
  private log(message: string) {
    this.MessageService.add(`NeedService: ${message}`);
  }

  // HTTP options for POST and PUT requests
  httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };


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


  /**
   * gets the needs from the back-end
   * 
   * @returns returns the GET request observable
   */
  getNeeds(): Observable<Need[]> {

    // sends a GET request to the needsUrl
    return this.http.get<Need[]>(this.needsUrl);
  }


  /**
   * updates a single need in the needs cupboard
   * 
   * @param need - need to be updated
   * @param name - name of updated need
   * @param value - value of updated need
   * @param currentQuantity - currentQuantity of updated need
   * @param maxQuantity - currentQuantity of updated need
   * @param type - type of updated need
   * 
   * @returns returns the PUT request observable
   */
  updateNeed(need:any, name: string, value: number, currentQuantity: number, maxQuantity: number, type: string, visible: boolean):Observable<any>{
    
    // creates the new need object with updated values
    const updatedNeed: Need = {id: need.id, name, value, currentQuantity, maxQuantity, type, visible};
    
    // sends a PUT request to the needsUrl with the updated need object
    return this.http.put<Need>(this.needsUrl, updatedNeed).pipe(
      tap(_ => {
        this.log(`updated need id=${need.id}`);
      }),
      catchError(this.handleError<any>('updateNeed'))
    );
  } 

  /** PUT: update the need's visibility on the server */
  changeNeedVisibility( id: number, visible: boolean): Observable<any> {
    const url = `${this.needsUrl}/${id}/visible/?visible=${visible}`;

    return this.http.put(url, this.httpOptions).pipe(
      tap(_ => {
        this.log(`updated need id=${id}`);
      }),
      catchError(this.handleError<any>('updateNeed'))
    );
  }

  /**
   * creates a single need in the needs cupboard
   * 
   * @param need - need to be created
   * @returns returns the POST request observable
   */
  createNeed(need: any): Observable<any> {

    // sends a POST request to the needsUrl with the need object
    return this.http.post<Need>(this.needsUrl, need, this.httpOptions).pipe(
      tap((newNeed: Need) => {
        this.log(`added need w/ id=${newNeed.id}`);
      }),
      catchError(this.handleError<Need>('addNeed'))
      );
  }

  /**
   * deletes a single need from the needs cupboard
   * 
   * @param needId - id of need to be deleted
   * @returns returns the DELETE request observable
   */
  deleteNeedID(needId: number): Observable<any> {

    // sends a DELETE request to the needsUrl with the need's id
    return this.http.delete<Need>(`${this.needsUrl}/${needId}`).pipe(
      tap(_ => {
        this.log(`deleted need id=${needId}`);
      }),
      catchError(this.handleError<Need>('deleteNeed'))
    );
  }

}
