import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

import { MessageService } from './message';

export interface Review {
  id: number;
  userName: string;
  reviewText: string;
  rating: number;
}

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  private reviewsUrl = 'http://localhost:8080/reviews';

  constructor(
    private http: HttpClient,
    private MessageService: MessageService
  ) {}

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  /** GET: all reviews */
  getReviews(): Observable<Review[]> {
    return this.http.get<Review[]>(this.reviewsUrl).pipe(
      tap(_ => this.log('fetched reviews')),
      catchError(this.handleError<Review[]>('getReviews', []))
    );
  }

  /** GET: reviews with a specific rating (1..5) */
  getReviewsByRating(rating: number): Observable<Review[]> {
    const url = `${this.reviewsUrl}/rating/${rating}`;
    return this.http.get<Review[]>(url).pipe(
      tap(list => {
        if (list && list.length > 0) {
          this.log(`found ${list.length} review(s) with rating=${rating}`);
        } else {
          this.log(`no reviews with rating=${rating}`);
        }
      }),
      catchError(this.handleError<Review[]>('getReviewsByRating', []))
    );
  }

  //////// Save methods //////////

  /** POST: add a new review */
  addReview(data: { userName: string; reviewText: string; rating: number }): Observable<Review> {
    // Backend creates the real id
    const payload: Review = {
      id: 0,
      userName: data.userName,
      reviewText: data.reviewText,
      rating: data.rating
    };

    return this.http.post<Review>(this.reviewsUrl, payload, this.httpOptions).pipe(
      tap((newReview: Review) => this.log(`added review w/ id=${newReview.id}`)),
      catchError(this.handleError<Review>('addReview'))
    );
  }

  /** DELETE: delete one review by id */
  deleteReview(id: number): Observable<unknown> {
    const url = `${this.reviewsUrl}/${id}`;
    return this.http.delete(url, this.httpOptions).pipe(
      tap(_ => this.log(`deleted review id=${id}`)),
      catchError(this.handleError('deleteReview'))
    );
  }

  /** DELETE: delete all reviews */
  deleteAllReviews(): Observable<unknown> {
    return this.http.delete(this.reviewsUrl, this.httpOptions).pipe(
      tap(_ => this.log('deleted ALL reviews')),
      catchError(this.handleError('deleteAllReviews'))
    );
  }

  /** DELETE: delete all reviews by username */
  deleteReviewsByUser(userName: string): Observable<unknown> {
    const url = `${this.reviewsUrl}/user/${encodeURIComponent(userName)}`;
    return this.http.delete(url, this.httpOptions).pipe(
      tap(_ => this.log(`deleted reviews by user "${userName}"`)),
      catchError(this.handleError('deleteReviewsByUser'))
    );
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue by returning a safe result.
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      this.log(`${operation} failed: ${error.message}`);
      return of(result as T);
    };
  }

  /** Log helper */
  private log(message: string) {
    this.MessageService.add(`ReviewService: ${message}`);
  }
}
