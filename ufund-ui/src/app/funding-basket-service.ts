import { Injectable } from '@angular/core';

import { Observable, of } from 'rxjs';

import { Need } from './need';

import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class FundingBasketService {
  private needsUrl = 'http://localhost:8080/funding_basket_needs';  // URL to web api

  constructor(
    private http: HttpClient,
  ) { }



    /** GET needs from the server (BUG: CURRENTLY GETS FROM THE NEEDS CUPBOARD, HOWEVER THIS SHOULD GET FROM THE FUNDING BASKET)*/
  getNeeds(): Observable<Need[]> {
    return this.http.get<Need[]>(this.needsUrl);
  }


  // Save/Modify the Server methods //

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  /** PUT: update the hero on the server */
  updateNeed(need: Need): Observable<any> {
    return this.http.put(this.needsUrl, need, this.httpOptions)/*.pipe(
      tap(_ => this.log(`updated hero id=${hero.id}`)),
      catchError(this.handleError<any>('updateHero'))
    );*/
  }

  /** POST: add a new need to the server */
  addNeed(need: Need): Observable<Need> {
    return this.http.post<Need>(this.needsUrl, need, this.httpOptions)/*.pipe(
      tap((newHero: Need) => this.log(`added need w/ id=${newNeed.id}`)),
      catchError(this.handleError<Need>('addNeed'))
    );*/
  }

  /** DELETE: delete the need from the server */
  deleteNeed(id: number): Observable<Need> {
    const url = `${this.needsUrl}/${id}`;

    return this.http.delete<Need>(url, this.httpOptions)/*.pipe(
      tap(_ => this.log(`deleted hero id=${id}`)),
      catchError(this.handleError<Hero>('deleteHero'))
    );*/
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
      /*this.log(`${operation} failed: ${error.message}`);*/

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  /** Log a HeroService message with the MessageService *//*
  private log(message: string) {
    this.messageService.add(`HeroService: ${message}`);
  }*/
}
