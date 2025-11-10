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
          this.goToCheckout();
    } else {
      this.goToLogin();
    }
  }

  // Method for handling the checkout navigation
  goToCheckout() {
    this.router.navigate(['/checkout']);
  }

  // Method for handling the admin navigation
  goToAdmin(){
     this.router.navigate(['/admin']);
  }

  // Method for smooth scrolling to different sections of the homepage
  scrollTo(id: string, e?: Event) {
    e?.preventDefault();
    const el = document.getElementById(id);
    el?.scrollIntoView({ behavior: 'smooth', block: 'start' });
  }
}

