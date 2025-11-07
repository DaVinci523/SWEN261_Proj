import { Component } from '@angular/core';
import { User } from '../user';
import { Router } from '@angular/router';

@Component({
  selector: 'app-helper-page',
  standalone: false,
  templateUrl: './helper-page.html',
  styleUrl: './helper-page.css'
})
export class HelperPage {
  constructor(private router: Router, public user: User) {}

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
}
