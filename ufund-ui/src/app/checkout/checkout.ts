import { Component, OnInit, inject } from '@angular/core';
import { FundingBasketService } from '../funding-basket-service';
import { NeedsCupboardService } from '../needs-cupboard-service';
import { DonationService } from '../donation';
import { ModalService } from '../modal-service';

import { Router } from '@angular/router';

import { Need } from '../need';
import { User } from '../user';
import { interval, Subscription } from 'rxjs';

@Component({
  selector: 'app-checkout',
  standalone: false,
  templateUrl: './checkout.html',
  styleUrl: './checkout.css'
})
export class Checkout implements OnInit{

  private getNeedsSubscription: Subscription;
  
  constructor(
    private router: Router,
    private fundingBasketService: FundingBasketService,
    private needsCupboardService: NeedsCupboardService,
    private donationService: DonationService,
    private modal: ModalService
  ) {
    // Continuously call getNeeds on set intervals (ex: 5000 is 5 seconds) to keep them updated when page is not reloaded.
    this.getNeedsSubscription = interval(500).subscribe(() => {
      this.getNeeds();
      this.calculateTotals();
    })
  }

  user = inject(User);
  currentUser: string | null = null;
  userName: string = '';  // TO-DO: Set to the name of the currently logged in user 

  cartTotal:number = 0;
  donationTotal: number = 0

  userNeeds: Need[] = []; // the front-end container for the user's needs
  needsCupboard: Need[] = [];
  nameOnCard: string = "";
  cardNumber: string = "";
  address: string = "";

  getNeeds(): void { // gets the needs from the REST api by running a program in the needService
    this.fundingBasketService.getNeeds(this.userName).subscribe(userNeeds => 
    {
      this.userNeeds = userNeeds
      this.calculateTotals();
    }
    ); // retrieve a user's funding basket Needs
    this.needsCupboardService.getNeeds().subscribe(needsCupboard => this.needsCupboard = needsCupboard); // retrieve needsCupboard Needs  }

  }

  ngOnInit(): void {
    this.currentUser = this.user.getCurrentUser();
    if (!this.currentUser) {
      this.router.navigate(['/login'], { queryParams: { returnUrl: '/checkout' } });
      return;
    }
    this.userName = this.currentUser;
    this.getNeeds();
  }
  
  /**
   * Get all needs in the funding basket, then decrease the quantity of the respective needs in the needs cupboard by 
   * the funding basket quantities, then clear the funding basket. Update the donation values, show the total price of 
   * your cart broken down into both the donationTotal and the complete total
   */
  checkout(): void {
    this.modal.confirmDialog('Checkout', 'Confirm checkout?', 'Yes', 'No')
    .subscribe(ok => {
      if (ok) {
        this.nameOnCard = "";
        this.cardNumber = "";
        this.address = "";

        const updateRequests = [];

        // Compute totals
        this.cartTotal = 0;
        this.donationTotal = 0;

        
        this.userNeeds.forEach(userNeed => {
          const subtotal = userNeed.value * userNeed.maxQuantity;
          this.cartTotal += subtotal;
          if (userNeed.type === "donation") {
            this.donationTotal += subtotal;
          }
        });

        // Send donation to backend
        if (this.donationTotal > 0) {
          updateRequests.push(this.donationService.addDonation(this.donationTotal));
        }

        // Update cupboard + remove needs
        this.userNeeds.forEach(userNeed => {
          if (userNeed.type !== "donation") {
            this.needsCupboard.forEach(cupboardNeed => {
              if (cupboardNeed.id == userNeed.id && cupboardNeed.visible && userNeed.visible) {
                cupboardNeed.currentQuantity += userNeed.maxQuantity;
                updateRequests.push(this.needsCupboardService.updateNeed(cupboardNeed));
              }
            });
          }
          updateRequests.push(this.fundingBasketService.deleteNeed(this.userName, userNeed.id));
        });

        Promise.all(updateRequests.map(req => req.toPromise()))
          .then(() => {
            console.log("Checkout Completed Successfully");
            this.router.navigate(['/helper']); // go to helper page on checkout
          })
          .catch(err => console.log("Checkout Error:", err));
        
        this.router.navigate(['/helper']); // go to helper page on checkout
      }
    });
  }


  deleteNeed(need: Need): void {
    this.userNeeds = this.userNeeds.filter(n => n !== need); // removes the given need in the front end by re-defining the list by only filetering in needs that weren't given
    this.fundingBasketService.deleteNeed(this.userName, need.id).subscribe();
  }

  saveAll(): void {
    if (this.userNeeds) {
      for ( const need of this.userNeeds){ // update every need in the user's funding basket
      this.fundingBasketService.updateNeed(this.userName, need)
        .subscribe(() => {window.location.reload();});
      }
    }
  }
  
  /**
   * Calculate the cart total to be displayed
   */
  calculateTotals(): void {
  this.cartTotal = 0;
  this.donationTotal = 0;

  this.userNeeds.forEach(need => {
    if(need.visible){
    const subtotal = need.value * need.maxQuantity;
    this.cartTotal += subtotal;
    
    if (need.type === "donation") {
      this.donationTotal += subtotal;
    }
  }
  });
  console.log("Cart Total: $" + this.cartTotal);
  console.log("Donation Total: $" + this.donationTotal);
}

}
