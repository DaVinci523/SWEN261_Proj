import { Injectable } from '@angular/core';

import { Observable, of, BehaviorSubject } from 'rxjs';

import { Need } from './need';

import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';

import {MessageService} from './message';


@Injectable({
  providedIn: 'root'
})
export class FundingBasketService {
  private needsUrl = 'http://localhost:8080/funding_basket_needs';  // URL to web api

  // BehaviorSubject used for tracking the items in the cart
  private itemCountSubject = new BehaviorSubject<number>(0);
  itemCount$ = this.itemCountSubject.asObservable();

  constructor(
    private http: HttpClient,
    private MessageService: MessageService
  ) { }

  // Save/Modify the Server methods //
  
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  getUserNames(): Observable<any> {
    const url = `${this.needsUrl}/usernames`;

    return this.http.get(url);
  }

    /** GET needs from the server */
  getNeeds(userName: String): Observable<any> {
    const url = `${this.needsUrl}/${userName}/needs`;

    return this.http.get<any>(url);
  }

  /** 
   * GET need by id. Will 404 if id not found 
   */
  getNeed(userName: String, id: number): Observable<Need> {
    const url = `${this.needsUrl}/${userName}/${id}`;
    return this.http.get<Need>(url)
  }

  /** PUT: update the need on the server */
  updateNeed(userName: String, need: Need): Observable<any> {
    const url = `${this.needsUrl}/${userName}`;

    return this.http.put(url, need, this.httpOptions)
  }

  /** PUT: update the need's visibility on the server */
  changeNeedVisibility(userName: String, id: number, visible: boolean): Observable<any> {
    const url = `${this.needsUrl}/${userName}/${id}/visible/?visible=${visible}`;

    return this.http.put(url, this.httpOptions)
  }

  /** POST: add a new need to the server */
  addNeed(userName: String, need: Need): Observable<Need> {
    const url = `${this.needsUrl}/${userName}`;

    return this.http.post<Need>(url, need, this.httpOptions).pipe(
      tap(_ => this.updateItemCount(userName))
    );
  }

  /** DELETE: delete the need from the server */
  deleteNeed(userName: String, id: number): Observable<Need> {
    const url = `${this.needsUrl}/${userName}/${id}`;

    return this.http.delete<Need>(url, this.httpOptions).pipe(
      tap(_ => this.updateItemCount(userName))
    );
  }

  /**
   * Refreshes and updates the observable item count 
   */
  updateItemCount(userName: String): void {
    this.getNeeds(userName).subscribe({
      next: (needs) => {
        // loops through every need and adds their maxQuantity(the # of an item in the basket) to a sum
        const total = needs.reduce((sum: number, need: any) => {
          if(need.visible){ // only count visible needs
            return sum + (need.maxQuantity ?? 0);
          }
          else if(!need.visible){
            return sum;
          }
        }, 0);
        this.itemCountSubject.next(total);
      },
      error: (err) => {
        console.error('Failed to update the cart count:', err);
        this.itemCountSubject.next(0);
      }
    });
  }

  /**
   * Return the current number of items in the cart
   */
  getCurrentItemCount(): number {
    return this.itemCountSubject.value;
  }

  addDonation(userName: string, amount: number) {
  const donationNeed = {
    id: Date.now() * -1,
    name: "Donation",
    value: amount,
    currentQuantity: 0,
    maxQuantity: 1,
    type: "donation",
    visible: true
  };
  return this.addNeed(userName, donationNeed);
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

      console.error(error); // log to console instead

      this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  /** Log a FundingBasketService message with the MessageService */
  private log(message: string) {
    this.MessageService.add(`FundingBasketService: ${message}`);
  }
}
