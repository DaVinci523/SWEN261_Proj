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
    this.router.navigate(['/checkout']);
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }

}

