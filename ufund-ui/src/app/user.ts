import { Injectable } from '@angular/core';

export type Role = 'admin' | 'helper';

@Injectable({ providedIn: 'root' })
export class User {
  private USER_KEY = 'auth_username';
  private FLAG_KEY = 'auth_loggedin';

  loggedIn = false;
  currentUser: string | null = null;

  constructor() {
    const flag = sessionStorage.getItem(this.FLAG_KEY);
    const user = sessionStorage.getItem(this.USER_KEY);
    if (flag === 'true' && user) {
      this.loggedIn = true;
      this.currentUser = user;
    }
  }

  /** set session after backend validates/creates */
  setSession(username: string): void {
    this.loggedIn = true;
    this.currentUser = username;
    sessionStorage.setItem(this.FLAG_KEY, 'true');
    sessionStorage.setItem(this.USER_KEY, username);
  }

  logout(): void {
    this.loggedIn = false;
    this.currentUser = null;
    sessionStorage.removeItem(this.FLAG_KEY);
    sessionStorage.removeItem(this.USER_KEY);
  }

  getCurrentUser(): string | null {
    return this.currentUser;
  }

  getCurrentUserRole(): Role | null {
    if (!this.currentUser) {
      return null;
    }

    if (this.currentUser.toLowerCase() == 'admin') {
      return 'admin';
    }

    return 'helper';
  }

  isLoggedIn(): boolean {
    return this.loggedIn;
  }
}
