import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { Need } from '../need';
import { FundingBasketService } from '../funding-basket-service';
import { NeedsCupboardService } from '../needs-cupboard-service';
import { User } from '../user';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-funding-basket',
  standalone: false,
  templateUrl: './funding-basket.html',
  styleUrls: ['./funding-basket.css']
})
export class FundingBasket implements OnInit {
  constructor(
    public fundingBasketService: FundingBasketService,
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
    this.fundingBasketService.getNeeds(this.userName).subscribe((userNeeds) => {
      this.userNeeds = userNeeds;
      
      //var updated = false;

      this.needsCupboardService.getNeeds().subscribe(cupNeeds => {
        this.needsCupboard = cupNeeds;

        var updateCalls: any[] = [];

        // Update the data for each of the current user's needs to be the same as the cupboards data
        this.userNeeds.forEach(userNeed => {
          this.needsCupboard.forEach((cupboardNeed:Need) => { //loop through the cupboard's needs
              
              if(userNeed.id == cupboardNeed.id){ // update the respective data for each userNeed
                if(userNeed !== cupboardNeed){ // update only if there is a discrepency
                  //updated = true;
                  updateCalls.push(this.fundingBasketService.updateNeed( this.userName, { 
                    id: cupboardNeed.id, 
                    name: cupboardNeed.name, 
                    value: cupboardNeed.value, 
                    currentQuantity: userNeed.currentQuantity, 
                    maxQuantity: userNeed.maxQuantity, 
                    type: cupboardNeed.type, 
                    visible: cupboardNeed.visible
                  } as Need)); // reload the page after updating
                }
              }
          });
        });

        if (updateCalls.length > 0) {
          forkJoin(updateCalls).subscribe(() => {
            // Refresh data after all updates complete
            this.fundingBasketService.getNeeds(this.userName).subscribe((userNeeds) => this.userNeeds = userNeeds);
            this.needsCupboardService.getNeeds().subscribe(cupNeeds => this.needsCupboard = cupNeeds);
          });
        }

      });

      

    });
  }

  // add a Need
  add(name: string, value: number, quantity: number, type: string): void {
    this.displayValue = value;
    var id = this.needQuantity;
    name = name.trim();
    if (!name) { 
      return; 
    }
    this.fundingBasketService.addNeed(this.userName, { id, name, value, currentQuantity: 0, maxQuantity: 1, type, visible: true } as Need)
      .subscribe(need => {
        this.userNeeds.push(need);
        this.fundingBasketService.updateItemCount(this.userName);
      });
  }

  delete(need: Need): void {
    this.userNeeds = this.userNeeds.filter(n => n !== need); // removes the given need in the front end by re-defining the list by only filetering in needs that weren't given
    this.fundingBasketService.deleteNeed(this.userName, need.id).subscribe(() => {
      this.fundingBasketService.updateItemCount(this.userName);
    });
  }

  increaseQuantity(need: Need): void {
    //this.fundingBasketService.increaseQuantity(need).subscribe();
    need.maxQuantity++;; // temporary visual, does NOT affect the data.json file
    this.fundingBasketService.updateNeed(this.userName, need)
        .subscribe(() => {this.getNeeds();
          window.location.reload();
        });

  }

  decreaseQuantity(need: Need): void {
    //this.fundingBasketService.increaseQuantity(need).subscribe();
    need.maxQuantity--; // temporary visual, does NOT affect the data.json file
    if (need.maxQuantity == 0){
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