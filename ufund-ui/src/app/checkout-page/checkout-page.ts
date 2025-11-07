import { Component } from '@angular/core';
import { User } from '../user';

@Component({
  selector: 'app-checkout-page',
  standalone: false,
  templateUrl: './checkout-page.html',
  styleUrl: './checkout-page.css'
})
export class CheckoutPage {
  constructor(public user: User) {}
}
