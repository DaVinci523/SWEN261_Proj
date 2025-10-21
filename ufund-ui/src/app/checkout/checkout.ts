import { Component, OnInit } from '@angular/core';

import { FundingBasketService } from '../funding-basket-service';
import { NeedsCupboardService } from '../needs-cupboard-service';

import { Need } from '../need';

@Component({
  selector: 'app-checkout',
  standalone: false,
  templateUrl: './checkout.html',
  styleUrl: './checkout.css'
})
export class Checkout implements OnInit{
  constructor(
    private fundingBasketService: FundingBasketService,
    private needsCupboardService: NeedsCupboardService
  ) { }

  userNeeds: Need[] = []; // the front-end container for the user's needs
  needsCupboard: Need[] = [];
  userName: String = "helper123"; // TO-DO: Set to the name of the currently logged in user

  nameOnCard: string = "";
  cardNumber: string = "";
  address: string = "";

  getNeeds(): void { // gets the needs from the REST api by running a program in the needService
    this.fundingBasketService.getNeeds(this.userName).subscribe(userNeeds => this.userNeeds = userNeeds); // retrieve a user's funding basket Needs
    this.needsCupboardService.getNeeds().subscribe(needsCupboard => this.needsCupboard = needsCupboard); // retrieve needsCupboard Needs  }
  }

  ngOnInit(): void { // initiates getNeeds
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
        this.needsCupboard.forEach((cupboardNeed) => {
          if (cupboardNeed.id == userNeed.id){
            cupboardNeed.quantity -= userNeed.quantity;
            this.needsCupboardService.updateNeed(cupboardNeed).subscribe();
          }
        });
      });
    } catch (error){
      console.log("ERROR: " + error);
    }
  }
}
