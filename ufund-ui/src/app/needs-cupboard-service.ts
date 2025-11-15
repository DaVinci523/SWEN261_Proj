import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { BehaviorSubject, Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { Need } from './need';
import {MessageService} from './message';



@Injectable({
  providedIn: 'root'
})
export class NeedsCupboardService {
  private needsUrl = 'http://localhost:8080/needs'

  // BehaviorSubject used for tracking the needs in the needs cupboard
  private static cupboardSubject = new BehaviorSubject<Need[]>({} as Need[]);
  static cupboardNeeds$ = NeedsCupboardService.cupboardSubject.asObservable();

  constructor(
      private http: HttpClient,
      private MessageService: MessageService){}

  /** GET needs from the server */
  getNeeds(): Observable<any> {
    return this.http.get(this.needsUrl);
    // .pipe(
    //   tap(_ => {
    //     this.updateCupboardNeeds();
    //   })
    // );
  }

  httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };


  /** GET need by id. Will 404 if id not found */
  getNeed(id: number): Observable<Need> {
    const url = `${this.needsUrl}/${id}`;
    return this.http.get<Need>(url).pipe(
      tap(_ => {
        this.log(`fetched need id=${id}`);
        // this.updateCupboardNeeds();
    }),
      catchError(this.handleError<Need>(`getNeed id=${id}`))
    );
  }

  /* GET needs whose name contains search term */
  searchNeeds(term: string): Observable<Need[]> {
    if (!term.trim()) {
      // if not search term, return empty need array.
      return of([]);
    }
    return this.http.get<Need[]>(`${this.needsUrl}/name/${term}`).pipe(
      tap(x => x.length ?
         this.log(`found needs matching "${term}"`) :
         this.log(`no needs matching "${term}"`)),
      catchError(this.handleError<Need[]>('searchNeeds', []))
    );
  }

  /* GET needs whose type contains search term */
  searchNeedsType(term: string): Observable<Need[]> {
    if (!term.trim()) {
      // if not search term, return empty need array.
      return of([]);
    }
    return this.http.get<Need[]>(`${this.needsUrl}/type/${term}`).pipe(
      tap(x => x.length ?
         this.log(`found needs matching "${term}"`) :
         this.log(`no needs matching "${term}"`)),
      catchError(this.handleError<Need[]>('searchNeeds', []))
    );
  }

  /* GET needs whose quanity is equal or less than search term */
  searchNeedsQuantity(term: number): Observable<Need[]> {
    return this.http.get<Need[]>(`${this.needsUrl}/quantity/${term}`).pipe(
      tap(x => x.length ?
         this.log(`found needs matching "${term}"`) :
         this.log(`no needs matching "${term}"`)),
      catchError(this.handleError<Need[]>('searchNeeds', []))
    );
  }

  /* GET needs whose quanity is equal or less than search term */
  searchNeedsValue(term: number): Observable<Need[]> {
    return this.http.get<Need[]>(`${this.needsUrl}/value/${term}`).pipe(
      tap(x => x.length ?
         this.log(`found needs matching "${term}"`) :
         this.log(`no needs matching "${term}"`)),
      catchError(this.handleError<Need[]>('searchNeeds', []))
    );
  }


  //////// Save methods //////////

  /** POST: add a new need to the server */
  addNeed(need: Need): Observable<Need> {
    return this.http.post<Need>(this.needsUrl, need, this.httpOptions).pipe(
      tap((newNeed: Need) => {
        this.log(`added need w/ id=${newNeed.id}`);
        // this.updateCupboardNeeds();
    }),
      catchError(this.handleError<Need>('addNeed')),
    );
  }

  /** DELETE: delete the need from the server */
  deleteNeed(id: number): Observable<Need> {
    const url = `${this.needsUrl}/${id}`;

    return this.http.delete<Need>(url, this.httpOptions).pipe(
      tap(_ => {
        this.log(`deleted need id=${id}`);
        // this.updateCupboardNeeds();
    }),
      catchError(this.handleError<Need>('deleteNeed'))
    );
  }

  /** PUT: update the need on the server */
  updateNeed(need: Need): Observable<any> {
    return this.http.put(this.needsUrl, need, this.httpOptions).pipe(
      tap(_ => {
        this.log(`updated need id=${need.id}`);
        // this.updateCupboardNeeds();
    }),
      catchError(this.handleError<any>('updateNeed'))
    );
  }

  
  /**
   * Refreshes and updates the observable cupboard
   */
  updateCupboardNeeds(): void {
    this.getNeeds().subscribe({
      next: (needs) => { // updates the front-end needs with the backend
        NeedsCupboardService.cupboardSubject.next(needs);
      },
      error: (err) => {
        console.error('Failed to update the cupboard needs:', err);
        NeedsCupboardService.cupboardSubject.next({} as Need[]);
      }
    });
  }

  /**
   * Return the current cupboard needs
   */
  getCurrentUserNeeds(): Need[] {
    return NeedsCupboardService.cupboardSubject.value;
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

  /** Log a NeedService message with the MessageService */
  private log(message: string) {
    this.MessageService.add(`NeedService: ${message}`);
  }









}
