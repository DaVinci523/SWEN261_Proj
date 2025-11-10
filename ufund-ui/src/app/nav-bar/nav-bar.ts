import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../user';
import { FundingBasketService } from '../funding-basket-service';

@Component({
  selector: 'app-nav-bar',
  standalone: false,
  templateUrl: './nav-bar.html',
  styleUrl: './nav-bar.css'
})
export class NavBar  implements OnInit{
  cartItemCount: number = 0;

  constructor(private router: Router, public user: User, private fundingBasketService: FundingBasketService) {}

  ngOnInit(): void {
      if (this.user.isLoggedIn()) {
        const username = this.user.getCurrentUser();
        if (username) {
          // Refresh the count 
          this.fundingBasketService.updateItemCount(username);
          // Subscribe to live count updates
          this.fundingBasketService.itemCount$.subscribe(count => {
            this.cartItemCount = count;
        });
        }
      }
  }

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

  goToReviews(){
    this.router.navigate(['/reviews']);
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

