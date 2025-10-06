import { Component } from '@angular/core';
import { Router } from '@angular/router';


@Component({
  selector: 'app-home-page',
  standalone: false,
  templateUrl: './home-page.html',
  styleUrl: './home-page.css'
})
export class HomePage {
  constructor(private router: Router) { }

  // Method for handling the page navigation
  goToPage(){
    this.router.navigate(['/page']);
  }

  // Method for handling the checkout navigation
  goToCheckout() {
    this.router.navigate(['/checkout']);
  }
}
