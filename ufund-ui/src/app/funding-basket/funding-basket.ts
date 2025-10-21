import { Component, OnInit } from '@angular/core';

import { Need } from '../need';
import { FundingBasketService } from '../funding-basket-service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-funding-basket',
  standalone: false,
  templateUrl: './funding-basket.html',
  styleUrl: './funding-basket.css'
})
export class FundingBasket implements OnInit{
  constructor(private fundingBasketService: FundingBasketService) { }
  userNeeds: Need[] = []; // the front-end container for the user's needs
  needQuantity: number = 30; // A placeholder for dev testing adding new needs
  displayValue: number = 69;
  userName: String = "helper123"; // TO-DO: Set to the name of the currently logged in user

  
  getNeeds(): void { // gets the needs from the REST api by running a program in the needService
    this.fundingBasketService.getNeeds(this.userName).subscribe(userNeeds => this.userNeeds = userNeeds);
  }

  ngOnInit(): void { // initiates getNeeds
    this.getNeeds(); 
  }

  add(name: string, value: number, quantity: number, type: string): void {
    this.displayValue = value;
    var id = this.needQuantity;
    name = name.trim();
    if (!name) { 
      return; 
    }
    this.fundingBasketService.addNeed(this.userName, { id, name, value, quantity, type } as Need)
      .subscribe(need => {
        this.userNeeds.push(need);
      });
  }

  delete(need: Need): void {
    this.userNeeds = this.userNeeds.filter(n => n !== need); // removes the given need in the front end by re-defining the list by only filetering in needs that weren't given
    this.fundingBasketService.deleteNeed(this.userName, need.id).subscribe();
  }

  increaseQuantity(need: Need): void {
    //this.fundingBasketService.increaseQuantity(need).subscribe();
    need.quantity++;; // temporary visual, does NOT affect the data.json file
    this.fundingBasketService.updateNeed(this.userName, need)
        .subscribe(() => this.getNeeds());
  }

  decreaseQuantity(need: Need): void {
    //this.fundingBasketService.increaseQuantity(need).subscribe();
    need.quantity--; // temporary visual, does NOT affect the data.json file
    if (need.quantity == 0){
      this.fundingBasketService.deleteNeed(this.userName, need.id).subscribe(() => {
      window.location.reload();});
    }
    else{
    this.fundingBasketService.updateNeed(this.userName, need)
        .subscribe(() => this.getNeeds());
    }
  }

  saveAll(): void {
    if (this.userNeeds) {
      for ( const need of this.userNeeds){ // update every need in the user's funding basket
      this.fundingBasketService.updateNeed(this.userName, need)
        .subscribe(() => this.getNeeds());
      }
    }
  }

}