import { Injectable } from '@angular/core';

export type Role = 'admin' | 'helper';

@Injectable({
  providedIn: 'root'
})
export class User {
  usernames = new Map<string, Role>([
    ['helper123', 'helper'],['admin123', 'admin']
  ]);
  loggedIn = false;
  currentUser: string | null = null;

  login(username: string): boolean{
    this.loggedIn = true;
    this.currentUser = username;
    return true;
  }

  logout(): void {
    this.loggedIn = false;
    this.currentUser = "";
  }

  userExists(name: string): boolean {
    return this.usernames.has(name);
  }

  addUser(name: string) {
    if(!this.usernames.has(name)) {
      this.usernames.set(name, 'helper');
    }
  }

  getCurrentUser(): string | null{
    return this.currentUser;
  }

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

  isLoggedIn(): boolean {
    return this.loggedIn;
  }

}
