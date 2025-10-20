import { Injectable } from '@angular/core';

export type Role = 'admin' | 'helper';

export interface UserInfo {
  password: string;
  role: Role;
}

@Injectable({
  providedIn: 'root'
})
export class User {

  private USER_KEY = 'auth_username';
  private FLAG_KEY = 'auth_loggedin';

  constructor() {
    const flag = localStorage.getItem(this.FLAG_KEY) === 'true';
    const user = localStorage.getItem(this.USER_KEY);
    if (flag && user) {
      this.loggedIn = true;
      this.currentUser = user;
    }
  }


  //List of usernames + their role
  private users = new Map<string, UserInfo>([
    ['helper123', { password: 'pw1', role: 'helper' }],
    ['admin123',  { password: 'pw2', role: 'admin'  }],
  ]);
  //Checks if a user is logged in (used across componenets)
  loggedIn = false;
  //Current user logged in (username) and can be used to get their role
  currentUser: string | null = null;

  //Makes it known a user is logged in and who is
  login(username: string, password: string): boolean {
    if (!username || !password) {return false};
    this.loggedIn = true;
    this.currentUser = username.trim();
    localStorage.setItem(this.FLAG_KEY, 'true');
    localStorage.setItem(this.USER_KEY, this.currentUser);
    return true;
  }



  //Logs out a user
  logout(): void {
    this.loggedIn = false;
    this.currentUser = null;        
    localStorage.removeItem(this.FLAG_KEY);
    localStorage.removeItem(this.USER_KEY);
  }


  //Checks if a username exists
  userExists(name: string): boolean {
    return this.users.has(name);
  }

  //Add a username (always helper)
  addUser(username: string, password: string, role: Role = 'helper'): boolean {
    if (this.users.has(username)) {
      return false;
    }

    this.users.set(username, { password, role });
    return true;
  }

  //Get current username
  getCurrentUser(): string | null{
    return this.currentUser;
  }

  //Get current user role
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


  //Checks if a user is logged in
  isLoggedIn(): boolean {
    return this.loggedIn;
  }

}
