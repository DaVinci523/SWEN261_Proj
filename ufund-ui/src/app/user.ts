import { Injectable } from '@angular/core';

export type Role = 'admin' | 'helper';
export interface UserInfo { password: string; role: Role; }
export interface PublicUserInfo { username: string; password: string; role: Role; }
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
  const creds = this.getCreds();
    if (!creds['admin']) {
      creds['admin'] = { password: 'admin', role: 'admin' };
      this.setCreds(creds);
    }
    const flag = localStorage.getItem(this.FLAG_KEY);
    const user = localStorage.getItem(this.USER_KEY);
    if (flag === 'true' && user) {
      this.loggedIn = true;
      this.currentUser = user;
    } else {
      this.loggedIn = false;
      this.currentUser = null;
    }
  }


  getAllUsers() {
    const creds = this.getCreds(); //map
    const list: PublicUserInfo[] = []; //store
    const keys = Object.keys(creds); //usernames
    let i = 0;
    while (i < keys.length) {
      const uname = keys[i];
      const rec = creds[uname]; //record for that username
      if (rec) {
        list.push({ username: uname, password: rec.password, role: rec.role });
      }
      i++;
    }
    return list;
  }

  deleteUser(username: string): boolean {
    if (!username) {
      return false;
    }
    const uname = username.trim();
    if (!uname) {
      return false;
    }

    const lower = uname.toLowerCase();
    if (lower == 'admin') {
      return false;
    }

    const creds = this.getCreds();
    if (!creds[uname]) {
      return false;
    }
    delete creds[uname];
    this.setCreds(creds);
    if (this.currentUser == uname) {
      this.logout();
    }
    return true;
  }
  
  clearAllUsers(){
    localStorage.removeItem(this.CREDS_KEY);

    //readd admin
    const seed: CredStore = {};
    seed['admin'] = { password: 'admin', role: 'admin' };
    this.setCreds(seed);
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

  userExists(username: string): boolean {
    if (username == null) {
      return false;
    }

    const uname = username.trim();
    if (!uname) {
      return false;
    }

    const creds = this.getCreds();
    if (creds[uname] !== undefined) {
      return true;
    } else {
      return false;
    }
  }

  createUser(username: string, password: string): boolean {
    if (username == null) {
      return false;
    }
    if (password == null) {
      return false;
    }

    const uname = username.trim();
    if (!uname) {
      return false;
    }
    if (!password) {
      return false;
    }

    const creds = this.getCreds();
    if (creds[uname]) {
      return false;
    }

    const role = this.getRoleFor(uname);
    creds[uname] = { password: password, role: role };
    this.setCreds(creds);
    return true;
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
    const rec = creds[uname];

    if (!rec) {
      return false;
    }
    if (rec.password !== password) {
      return false;
    }

    this.loggedIn = true;
    this.currentUser = uname;
    localStorage.setItem(this.FLAG_KEY, 'true');
    localStorage.setItem(this.USER_KEY, uname);
    return true;
  }

  /**
   * "Logs out a user" which clears the current user logged in.
   */
  logout(): void {
    this.loggedIn = false;
    this.currentUser = null;
    localStorage.removeItem(this.FLAG_KEY);
    localStorage.removeItem(this.USER_KEY);
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
