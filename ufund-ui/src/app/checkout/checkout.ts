import { Component, OnInit, inject } from '@angular/core';

import { FundingBasketService } from '../funding-basket-service';
import { NeedsCupboardService } from '../needs-cupboard-service';
import { Router } from '@angular/router';

import { Need } from '../need';
import { User } from '../user';

@Component({
  selector: 'app-checkout',
  standalone: false,
  templateUrl: './checkout.html',
  styleUrl: './checkout.css'
})
export class Checkout implements OnInit{
  constructor(
    private router: Router,
    private fundingBasketService: FundingBasketService,
    private needsCupboardService: NeedsCupboardService
  ) { }

  user = inject(User);
  currentUser: string | null = null;
  userName: string = '';  // TO-DO: Set to the name of the currently logged in user 


  userNeeds: Need[] = []; // the front-end container for the user's needs
  needsCupboard: Need[] = [];
  nameOnCard: string = "";
  cardNumber: string = "";
  address: string = "";

  getNeeds(): void { // gets the needs from the REST api by running a program in the needService
    this.fundingBasketService.getNeeds(this.userName).subscribe(userNeeds => this.userNeeds = userNeeds); // retrieve a user's funding basket Needs
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
  
  // Get all needs in the funding basket, (done)
  // then decrease the quantity of the respective needs in the needs cupboard by the funding basket quantities, 
  // then clear the funding basket
  checkout(): void{
    console.log("RUNNING")
    this.nameOnCard = ""; // reset the data to empty strings.
    this.cardNumber = "";
    this.address = "";

    console.log("USERNEEDS")
    console.log(this.userNeeds);
    console.log("CUPBOARD")
    console.log(this.needsCupboard);
    try{
      this.userNeeds.forEach((userNeed) => { // loop through every userNeed
        console.log("USERNEED")
        console.log(userNeed);
        this.needsCupboard.forEach((cupboardNeed) => {
          if (cupboardNeed.id == userNeed.id && cupboardNeed.visible && userNeed.visible){ // update NeedsCupbard needs if both are of same id, and both are visible.
            cupboardNeed.currentQuantity += userNeed.maxQuantity;
            console.log("CUPBOARD")
            console.log(cupboardNeed)
            this.needsCupboardService.updateNeed(cupboardNeed).subscribe();

            this.deleteNeed(userNeed);
          }
        });
        console.log("BASKET");
        console.log(this.userNeeds);
        console.log("NEEDCUP");
        console.log(this.needsCupboard);

        this.saveAll();

      }); 
      
      window.location.reload(); // reload the page

    } catch (error){
      console.log("ERROR: " + error);
    }

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
  
}
