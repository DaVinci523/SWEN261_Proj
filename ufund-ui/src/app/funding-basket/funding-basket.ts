import { Component, OnInit } from '@angular/core';

import { Need } from '../need';
import { FundingBasketService } from '../funding-basket-service';

@Component({
  selector: 'app-funding-basket',
  standalone: false,
  templateUrl: './funding-basket.html',
  styleUrl: './funding-basket.css'
})
export class FundingBasket implements OnInit{
  constructor(private fundingBasketService: FundingBasketService) { }
  userNeeds: Need[] = []; // the front-end container for the user's needs
  needQuantity: number = 0;

  getNeeds(): void { // gets the needs from the REST api by running a program in the needService
    this.fundingBasketService.getNeeds().subscribe(userNeeds => this.userNeeds = userNeeds);
  }

  ngOnInit(): void { // initiates getNeeds
    this.getNeeds(); 
    
  }

  add(name: string, value: number, quantity: number, type: string): void {
    name = name.trim();
    if (!name) { 
      return; 
    }
    this.fundingBasketService.addNeed({ name } as Need)
      .subscribe(hero => {
        this.userNeeds.push(hero);
      });
  }

  delete(need: Need): void {
    this.userNeeds = this.userNeeds.filter(n => n !== need); // removes the given need in the front end by re-defining the list by only filetering in needs that weren't given
    this.fundingBasketService.deleteNeed(need.id).subscribe();
  }

  increaseQuantity(need: Need): void {
    this.needQuantity++; // temporary visual
  }

  decreaseQuantity(need: Need): void {
    this.needQuantity--; // temporary visual
  }

}