import { Injectable } from '@angular/core';

export type Role = 'admin' | 'helper';

@Injectable({
  providedIn: 'root'
})
export class User {
  //List of usernames + their role
  usernames = new Map<string, Role>([
    ['helper123', 'helper'],['admin123', 'admin']
  ]);
  //Checks if a user is logged in (used across componenets)
  loggedIn = false;
  //Current user logged in (username) and can be used to get their role
  currentUser: string | null = null;

  //Makes it known a user is logged in and who is
  login(username: string): boolean{
    this.loggedIn = true;
    this.currentUser = username;
    return true;
  }

  //Logs out a user
  logout(): void {
    this.loggedIn = false;
    this.currentUser = "";
  }

  //Checks if a username exists
  userExists(name: string): boolean {
    return this.usernames.has(name);
  }

  //Add a username (always helper)
  addUser(name: string) {
    if(!this.usernames.has(name)) {
      this.usernames.set(name, 'helper');
    }
  }

  //Get current username
  getCurrentUser(): string | null{
    return this.currentUser;
  }

  //Get current user role
  getCurrentUserRole(): string | null{
    if (!this.currentUser) {
      return null;
    }
    const role = this.usernames.get(this.currentUser);
    if (role == undefined) {
      return null;
    } 
    return role;
  }

  //Checks if a user is logged in
  isLoggedIn(): boolean {
    return this.loggedIn;
  }

}
