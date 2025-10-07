import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../user';

@Component({
  selector: 'app-home-page',
  standalone: false,
  templateUrl: './home-page.html',
  styleUrl: './home-page.css'
})
export class HomePage {
  constructor(private router: Router, public user: User) { }

  goToLogin(){
    this.router.navigate(['/login']);
  }

  // Method for handling the page navigation
  goToPage(){
    if(this.user.isLoggedIn()) {
          this.router.navigate(['/page']);
    } else {
      this.router.navigate(['/login']);
    }
  }

  // Method for handling the checkout navigation
  goToCheckout() {
    this.router.navigate(['/checkout']);
  }
}