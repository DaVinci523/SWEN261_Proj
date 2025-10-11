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
   * Fetches a specific need by its id
   * @param The id of the need to fetch
   * @return An observable of the Need object
   */
  getNeedsByID(id: number): Observable<Need> {
    const url = `${this.needsUrl}/${id}`;
    return this.http.get<Need>(url).pipe(
      tap(() => console.log(`fetched need id=${id}`)),
      catchError(this.handleError<Need>(`getNeedById id=${id}`))
    );
  }

  /**
   * Searches for needs based on their name
   * @param The name string to match with other need names
   * @return An observable array of all matching Need objects
   */
  searchNeeds(name: string): Observable<Need[]> {
    if (!name.trim()) {
    // if not search term, return empty hero array.
    return of([]);
    }
    const url = `${this.needsUrl}/?name=${name}`;
    return this.http.get<Need[]>(url).pipe(
      tap(x => x.length ?
        this.log(`found needs matching "${name}"`) :
        this.log(`no needs matching "${name}"`)),
      catchError(this.handleError<Need[]>('searchHeroes', []))
    );
  }

  /**
   * Creates a new need
   * @param the need object to create
   * @returns Observable of the created Need
   */
  createNeed(need: Need): Observable<Need> {
    return this.http.post<Need>(this.needsUrl, need, this.httpOptions).pipe(
      tap((newNeed: Need) => this.log(`added need w/ id=${newNeed.id}`)),
      catchError(this.handleError<Need>('createNeed'))
    );
  }

  /**
   * Updates an existing need 
   * @param The need object with updated values
   * @returns An observable of the updated need
   */
  updateNeed(need: Need): Observable<any> {
    return this.http.put(this.needsUrl, need, this.httpOptions).pipe(
      tap(_ => this.log(`updated hero id=${need.id}`)),
      catchError(this.handleError<any>('updateNeed'))
    );
  }

  /**
   * Deletes a need by its id
   * @param id the id of the need delete
   * @returs the observable of void
   */
  deleteNeedById(id: number): Observable<void> {
    const url = `${this.needsUrl}/${id}`;
    return this.http.delete<void>(url, this.httpOptions).pipe(
      tap(() => console.log(`Deleted need id=${id}`)),
      catchError(this.handleError<void>('deleteNeedById'))
    );
  }

  /**
   * Deletes all needs of a type
   * @param type the type of the needs to delete
   * @returns observable of void
   */
  deleteNeedByType(type: string): Observable<void> {
    const url = `${this.needsUrl}/${type}`;
    return this.http.delete<void>(url, this.httpOptions).pipe(
      tap(() => console.log(`Deleted needs type=${type}`)),
      catchError(this.handleError<void>('deleteNeedByType'))
    );
  }

  /**
   * Deletes all needs whose names contain the string
   * @param name the type of the needs to delete
   * @returns observable of void
   */
  deleteNeedByName(name: string): Observable<void> {
    const url = `${this.needsUrl}/name/${name}`;
    return this.http.delete<void>(url, this.httpOptions).pipe(
      tap(() => console.log(`Deleted needs with name matching=${name}"`)),
      catchError(this.handleError<void>('deleteNeedByType'))
    );
  }

  /**
   * Deletes all needs in the cupboard
   * @returns observable of void
   */
  deleteAllNeeds(): Observable<void> {
    return this.http.delete<void>(this.needsUrl, this.httpOptions).pipe(
      tap(() => console.log('Deleted all needs')),
      catchError(this.handleError<void>('deleteAllNeeds'))
    );
  }

  /**
   * Logs a NeedsCupboardService message
   * @param The message to log
   */
  private log(message: string){
    console.log(`NeedsCupboardService: ${message}`);
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
