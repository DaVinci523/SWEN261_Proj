import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DonationService {

  private donationUrl = 'http://localhost:8080/donations'; // backend controller url
  
  constructor(private http: HttpClient) { }

  /** Add to the total donation amount on backend */
  addDonation(amount: number): Observable<any> {
    return this.http.post(`${this.donationUrl}/add/${amount}`, {});
  }

  /** Get total + goal */
  getStatus(): Observable<any> {
    return this.http.get(`${this.donationUrl}/status`);
  }

  /** Set goal for admin */
  setGoal(goal: number): Observable<any> {
    return this.http.post(`${this.donationUrl}/goal/${goal}`, {});
  }
}
