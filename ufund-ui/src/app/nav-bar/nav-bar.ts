import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../user';

@Component({
  selector: 'app-nav-bar',
  standalone: false,
  templateUrl: './nav-bar.html',
  styleUrl: './nav-bar.css'
})
export class NavBar {
  constructor(private router: Router, public user: User) {}

  goToHome() {
    this.router.navigate(['']);
  }

  goToCheckout() {
    if(this.user.isLoggedIn()) {
      this.router.navigate(['/checkout']);
    } else {
      this.router.navigate(['/login']);
    }
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }

  onUserIconClick() {
    if (this.user.isLoggedIn()) {
      const ok = confirm(`Signed in as ${this.user.getCurrentUser()}\n\nLog out?`);
      if (ok) {
        this.user.logout();
        alert('You have been logged out.');
      }
    } else {
      this.goToLogin();
    }
  }


}

