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

  /**
   * Navigates to homepage
   */
  goToHome() {
    this.router.navigate(['']);
  }

  /**
   * Navigates to admin page
   */
  goToAdmin() {
    this.router.navigate(['/admin']);
  }

  /**
   * Navigates to checkout page if user is logged in,
   * otherwise it will send the user to the login page.
   */
  goToCheckout() {
    if(this.user.isLoggedIn()) {
      this.router.navigate(['/checkout']);
    } else {
      this.router.navigate(['/login']);
    }
  }

   /**
   * Navigates to login page.
   */
  goToLogin() {
    this.router.navigate(['/login']);
  }

   /**
   * Navigates to checkout page if user is logged in,
   * otherwise it will send the user to the helper/needs page.
   */
  goToHelper(){
    if(this.user.isLoggedIn()) {
      this.router.navigate(['/helper']);
    } else {
      this.router.navigate(['/login']);
    }
  }

   /**
   * When a user clicks the login icon in the navigation bar
   * If the user is logged in, ask them if they want to log out
   * If the user isn't logged in direct them to the login page
   */
  onUserIconClick() {
    if (this.user.isLoggedIn()) {
      const ok = confirm(`Signed in as ${this.user.getCurrentUser()}\n\nLog out?`);
      if (ok) {
        this.user.logout();
        alert('You have been logged out.');
        this.router.navigate(['/homepage']);
      }
    } else {
      this.goToLogin();
    }
  }


}

