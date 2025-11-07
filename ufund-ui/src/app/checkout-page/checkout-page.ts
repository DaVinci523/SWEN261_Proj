import { Component } from '@angular/core';
import { User } from '../user';
import { FundingBasketService } from '../funding-basket-service';


@Component({
  selector: 'app-checkout-page',
  standalone: false,
  templateUrl: './checkout-page.html',
  styleUrl: './checkout-page.css'
})
export class CheckoutPage {
  constructor(public user: User) {}
}
