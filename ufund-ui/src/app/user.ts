import { Injectable } from '@angular/core';

export type Role = 'admin' | 'helper';
export interface UserInfo { password: string; role: Role; }

@Injectable({ providedIn: 'root' })
export class User {
  private USER_KEY = 'auth_username';
  private FLAG_KEY = 'auth_loggedin';

  // auth session state
  loggedIn = false;
  currentUser: string | null = null;

  constructor() {
    if (localStorage.getItem(this.FLAG_KEY) || localStorage.getItem(this.USER_KEY)) {
      localStorage.removeItem(this.FLAG_KEY);
      localStorage.removeItem(this.USER_KEY);
    }

    // Restore from sessionStorage so refresh keeps it and new tabs won’t have it
    const flag = sessionStorage.getItem(this.FLAG_KEY) === 'true';
    const user = sessionStorage.getItem(this.USER_KEY);

    if (flag && user) {
      this.loggedIn = true;
      this.currentUser = user;
    }
  }

  login(username: string, password: string): boolean {
    if (!username || !password) { return false; }
    this.loggedIn = true;
    this.currentUser = username.trim();

    sessionStorage.setItem(this.FLAG_KEY, 'true');
    sessionStorage.setItem(this.USER_KEY, this.currentUser);
    return true;
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
    const u = this.currentUser;
    if (!u || u.trim() === '') {
       return null;
    }
    const name = u.trim().toLowerCase();
    if (name === 'admin') { 
      return 'admin'; 
    }
    return 'helper';
  }

  isLoggedIn(): boolean { 
    return this.loggedIn; 
  }
}
