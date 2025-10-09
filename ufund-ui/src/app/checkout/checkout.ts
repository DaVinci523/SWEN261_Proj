import { Component } from '@angular/core';

@Component({
  selector: 'app-checkout',
  standalone: false,
  templateUrl: './checkout.html',
  styleUrl: './checkout.css'
})
export class Checkout {
  nameOnCard: string = "";
  cardNumber: string = "";
  address: string = "";


  checkout(): void {
    this.nameOnCard = "";
    this.cardNumber = "";
    this.address = "";  

    
  }
}
