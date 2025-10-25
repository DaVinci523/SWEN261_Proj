import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { Need } from '../need';
import { FundingBasketService } from '../funding-basket-service';
import { NeedsCupboardService } from '../needs-cupboard-service';
import { User } from '../user';

@Component({
  selector: 'app-funding-basket',
  standalone: false,
  templateUrl: './funding-basket.html',
  styleUrls: ['./funding-basket.css']
})
export class FundingBasket implements OnInit {
  constructor(
    private fundingBasketService: FundingBasketService,
    private needsCupboardService: NeedsCupboardService,
    private router: Router
  ) {}

  user = inject(User);

  userNeeds: Need[] = [];
  needsCupboard: Need[] = [];
  needQuantity = 30;
  displayValue = 69;

  currentUser: string | null = null;
  userName: string = '';

  ngOnInit(): void {
    this.currentUser = this.user.getCurrentUser();
    if (!this.currentUser) {
      this.router.navigate(['/login'], { queryParams: { returnUrl: '/checkout' } });
      return;
    }
    this.userName = this.currentUser;
    this.getNeeds();
  }

  getNeeds(): void {
    this.fundingBasketService.getNeeds(this.userName).subscribe(userNeeds => this.userNeeds = userNeeds);
    this.needsCupboardService.getNeeds().subscribe(cupNeed => this.needsCupboard = cupNeed);
  }

  // updateNeeds(): boolean{ 
  //   try{
  //     console.log(this.userNeeds)
  //     this.userNeeds.forEach((userNeed) => { // loop through every userNeed
  //       console.log("USERNEED")
  //       console.log(userNeed);
  //       this.needsCupboard.forEach((cupboardNeed) => {
  //         if (cupboardNeed.id == userNeed.id && !cupboardNeed.visible && userNeed.visible){ // update NeedsCupbard needs if both are of same id, and both are visible.
  //           console.log("CUPBOARD")
  //           console.log(cupboardNeed)
  //           this.fundingBasketService.changeNeedVisibility(this.userName, userNeed.id, false);
  //         }
  //       });

  //       this.saveAll();

  //     }); 
            
  //     return true;

  //   } catch (error){
  //     console.log("ERROR: " + error);
      
  //     return false;
  //   }
  // }

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
        .subscribe(() => {this.getNeeds();
          window.location.reload();
        });

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
        .subscribe(() =>{ this.getNeeds(); 
          window.location.reload(); 
        });
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