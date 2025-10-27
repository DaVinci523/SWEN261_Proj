import { Injectable } from '@angular/core';

export type Role = 'admin' | 'helper';
export interface UserInfo { password: string; role: Role; }
type CredStore = Record<string, UserInfo>;

@Injectable({ providedIn: 'root' })
export class User {
  private USER_KEY = 'auth_username';
  private FLAG_KEY = 'auth_loggedin';
  private CREDS_KEY = 'auth_creds';  

  // auth session state
  loggedIn = false;
  currentUser: string | null = null;

  constructor() {
    // restore  session across tab refresh but not localhost open
    const flag = sessionStorage.getItem(this.FLAG_KEY);
    const user = sessionStorage.getItem(this.USER_KEY);

    if (flag === 'true' && user) {
      this.loggedIn = true;
      this.currentUser = user;
    } else {
      this.loggedIn = false;
      this.currentUser = null;
    }
  }

  private getCreds(): CredStore{
    const raw = localStorage.getItem(this.CREDS_KEY);
    let store = {};
    if (raw !== null) {
      const parsed = JSON.parse(raw);
      if (parsed) {
        store = parsed;
      }
    }
    return store;
  }

  private setCreds(creds: CredStore) {
    localStorage.setItem(this.CREDS_KEY, JSON.stringify(creds));
  }

  private getRoleFor(uname: string): Role {
    const lower = uname.toLowerCase();
    if (lower === 'admin') {
      return 'admin';
    }
    return 'helper';
  }

  /** 
   * "Logs in a user" which stores the current user logged in.
   * @param username Username of the user
   * @param password Password for username
   * @returns 
   */
  login(username: string, password: string): boolean {
    if (!username) {
      return false;
    }
    if (!password) {
       return false;
    }

    const uname = username.trim();
    if (!uname) { 
      return false;
    }

    const creds = this.getCreds();
    const exists = (creds as CredStore)[uname];

    if (exists === undefined) {
      const role = this.getRoleFor(uname);
      (creds as CredStore)[uname] = { password: password, role: role };
      this.setCreds(creds as CredStore);
    } else {
      if (exists.password !== password) {
        return false;
      }
    }

    this.loggedIn = true;
    this.currentUser = uname;
    sessionStorage.setItem(this.FLAG_KEY, 'true');
    sessionStorage.setItem(this.USER_KEY, uname);
    return true;
  }

  /**
   * "Logs out a user" which clears the current user logged in.
   */
  logout(): void {
    this.loggedIn = false;
    this.currentUser = null;
    sessionStorage.removeItem(this.FLAG_KEY);
    sessionStorage.removeItem(this.USER_KEY);
  }

  /**
   * Get the current user logged in
   * @returns Current user (their username) logged in
   */
  getCurrentUser(): string | null { 
    return this.currentUser;
   }

   /**
    * Get the role (admin / helper) of the current user logged in
    * @returns The logged in user's role
    */
  getCurrentUserRole(): Role | null {
    if (this.currentUser == null) {
      return null;
    }

    const creds = this.getCreds();
    const rec = creds[this.currentUser];

    if (rec) {
      return rec.role;
    }

    const lower = this.currentUser.toLowerCase();
    let role: Role = 'helper';
    if (lower === 'admin') {
      role = 'admin';
    }
    return role;
  }

  /**
   * @returns If a user is logged in
   */
  isLoggedIn(): boolean { 
    return this.loggedIn; 
  }
}
