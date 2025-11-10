import { Injectable, NgZone  } from '@angular/core';
import { Router } from '@angular/router';
import { AccountsService } from './accounts-service';


export type Role = 'admin' | 'helper';


@Injectable({ providedIn: 'root' })
export class User {
  private USER_KEY = 'auth_username';
  private FLAG_KEY = 'auth_loggedin';


  loggedIn = false;
  currentUser: string | null = null;


  private bc: BroadcastChannel | null = null;


  constructor(private router: Router, private ngZone: NgZone, private accounts: AccountsService) {
    const flag = sessionStorage.getItem(this.FLAG_KEY);
    const user = sessionStorage.getItem(this.USER_KEY);
    if (flag === 'true' && user) {
      this.loggedIn = true;
      this.currentUser = user;
    }

    if ('BroadcastChannel' in window) {
      try {
        this.bc = new BroadcastChannel('auth');
        this.bc.onmessage = (ev: MessageEvent) => {
          const data = ev.data;
          if (!data) { return; }
          if (data.type === 'force-logout') {
            const target = String(data.user || '');
            if (this.currentUser && target &&
                this.currentUser.toLowerCase() === target.toLowerCase()) {
              this.ngZone.run(() => {
                this.logout();
                try { this.router.navigate(['/login']); } catch (_e) {}
              });
            }
          }
        };
      } catch (_e) {
        this.bc = null;
      }
    }
    window.addEventListener('storage', (e: StorageEvent) => {
      if (!e.key) { return; }
      const prefix = 'force-logout:';
      if (e.key.indexOf(prefix) !== 0) { return; }
      const target = e.key.substring(prefix.length);
      if (!target) { return; }
      if (this.currentUser &&
          this.currentUser.toLowerCase() === target.toLowerCase()) {
        this.ngZone.run(() => {
          this.logout();
          try { this.router.navigate(['/login']); } catch (_e) {}
        });
      }
    });


    setInterval(() => {
      if (!this.loggedIn) { return; }
      if (!this.currentUser) { return; }
      const uname = this.currentUser;


      this.accounts.exists(uname).subscribe({
        next: (exists) => {
          if (!exists) {
            this.ngZone.run(() => {
              this.logout();
              try { this.router.navigate(['/login']); } catch (_e) {}
            });
          }
        },
        error: (_err) => {
        }
      });
    }, 4000);


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


