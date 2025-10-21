import { Component } from '@angular/core';
import { User } from '../user';

@Component({
  selector: 'app-helper-page',
  standalone: false,
  templateUrl: './helper-page.html',
  styleUrl: './helper-page.css'
})
export class HelperPage {
  constructor(public user: User) {}
}
