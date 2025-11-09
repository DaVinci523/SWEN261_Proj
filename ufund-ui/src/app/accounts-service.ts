import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { tap } from 'rxjs/operators';
import { Account } from './account';
import { MessageService } from './message';

@Injectable({ providedIn: 'root' })
export class AccountsService {
  private baseUrl = 'http://localhost:8080/accounts';

  private countSubject = new BehaviorSubject<number>(0);
  count$ = this.countSubject.asObservable();

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(
    private http: HttpClient,
    private message: MessageService
  ) {}

  /** GET: all accounts */
  getAccounts(): Observable<Account[]> {
    const url = `${this.baseUrl}`;
    return this.http.get<Account[]>(url).pipe(
      tap(list => this.countSubject.next(Array.isArray(list) ? list.length : 0))
    );
  }

  /** GET: usernames only */
  getUsernames(): Observable<string[]> {
    const url = `${this.baseUrl}/usernames`;
    return this.http.get<string[]>(url);
  }

  /** GET: does username exist */
  exists(username: string): Observable<boolean> {
    const url = `${this.baseUrl}/exists/${encodeURIComponent(username)}`;
    return this.http.get<boolean>(url);
  }

  /** POST: create account */
  create(account: Account): Observable<Account|null> {
    const url = `${this.baseUrl}`;
    return this.http.post<Account>(url, account, this.httpOptions).pipe(
      tap(_ => this.refreshCount()),
    );
  }

  /** POST: validate credentials */
  validate(username: string, password: string): Observable<boolean> {
    return this.http
      .post(`${this.baseUrl}/login`, { username, password }, { observe: 'response' })
      .pipe(
        map(resp => resp.status === 200),
        catchError(_ => of(false))
      );
  }

  /** DELETE: account by username */
  delete(username: string): Observable<void> {
    const url = `${this.baseUrl}/${encodeURIComponent(username)}`;
    return this.http.delete<void>(url, this.httpOptions).pipe(
      tap(_ => this.refreshCount()),
    );
  }

  /** Refresh the count, similar to basket count refresh */
  private refreshCount(): void {
    this.getAccounts().subscribe({
      next: (list) => this.countSubject.next(Array.isArray(list) ? list.length : 0),
      error: (_e) => this.countSubject.next(0)
    });
  }

  

  private log(msg: string) { this.message.add(`AccountsService: ${msg}`); }
}
