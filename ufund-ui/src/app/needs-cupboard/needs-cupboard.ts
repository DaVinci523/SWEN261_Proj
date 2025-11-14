import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { Need } from '../need';
import { NeedsCupboardService } from '../needs-cupboard-service';
import { FundingBasketService } from '../funding-basket-service';
import { DonationService } from '../donation';

import { User } from '../user';
import { interval, Subscription } from 'rxjs';

@Component({
  selector: 'app-needs-cupboard',
  standalone: false,
  templateUrl: './needs-cupboard.html',
  styleUrls: ['./needs-cupboard.css']
})
export class NeedsCupboard implements OnInit, OnDestroy {
  private getNeedsSubscription: Subscription;
  quantityTerm: any;

  constructor(
    private needsCupboardService: NeedsCupboardService,
    private fundingBasketService: FundingBasketService,
    private router: Router,
    private donationService: DonationService
  ) {
    // Continuously call getNeeds on set intervals (ex: 5000 is 5 seconds) to keep them updated when page is not reloaded.
    this.getNeedsSubscription = interval(4000).subscribe(() => {
      this.getNeeds();
      this.loadDonationStatus();
    })
  }

  user = inject(User);
  needFilledColor = "#bbffacff";

  userName: string = '';
  needs: Need[] = [];
  searchTerm = '';
  typeTerm = '';
  qtyTerm: number | null = null;
  valTerm: number | null = null;
  buttonText: string = 'Add to Cart';
  //used to display donations
  totalDonated: number = 0;
  donationGoal: number = 0;

  /**
   * Initialization
   * @returns 
   * 
   */
  ngOnInit(): void {
    const u = this.user.getCurrentUser();
    if (!u) {
      this.router.navigate(['/login'], { queryParams: { returnUrl: '/needs' } });
      return;
    }
    this.userName = u;
    this.getNeeds();
    this.loadDonationStatus();

    const saved = localStorage.getItem('buttonText');
    if (saved) this.buttonText = saved;
  }

  /**
   * Remove getNeedsSubscription if it still exists when this component is destroyed to prevent memory leaks.
   */
  ngOnDestroy(): void{
    if (this.getNeedsSubscription){
      this.getNeedsSubscription.unsubscribe();
    }
  }

  /**
   * Gathers full list of needs in needs cupboard JSON
   * If search bar has contents return needs of that name
   */
  getNeeds(): void {
    var method : any;
    console.log(this.searchTerm);
    console.log(this.typeTerm);
    console.log(this.qtyTerm);
    console.log(this.valTerm);


    
    if(this.qtyTerm != null && this.qtyTerm != 0){
      const term = this.qtyTerm;
      method = term
      ? this.needsCupboardService.searchNeedsQuantity(term)
      : this.needsCupboardService.getNeeds();
    }
    else if(this.valTerm != null && this.valTerm != 0){
      const term = this.valTerm;
      method = term
      ? this.needsCupboardService.searchNeedsValue(term)
      : this.needsCupboardService.getNeeds();
    }
    else if(this.typeTerm != null && this.typeTerm != ''){
      const term = this.typeTerm.trim();
      method = term
      ? this.needsCupboardService.searchNeedsType(term)
      : this.needsCupboardService.getNeeds();
    }
    else if(this.searchTerm != null){
      const term = this.searchTerm.trim();
      method = term
      ? this.needsCupboardService.searchNeeds(term)
      : this.needsCupboardService.getNeeds();
    }
    method.subscribe((needs: Need[]) => this.needs = needs);
  }

  /**
   * Search for needs based off type
   */
  searchNeedsType(): void{
    const typeTerm = this.typeTerm.trim();
    const method = typeTerm
    ? this.needsCupboardService.searchNeedsType(typeTerm)
    : this.needsCupboardService.getNeeds();
    method.subscribe(needs => this.needs = needs);

  }

   /**
   * Search for needs based off quantity
   */
  searchNeedsQuantity(): void{
    const qtyTerm = this.qtyTerm;
    if (this.qtyTerm === null || this.qtyTerm === undefined) {
    this.getNeeds();
    return;
  }
  const method = qtyTerm
  ? this.needsCupboardService.searchNeedsQuantity(this.qtyTerm)
  : this.needsCupboardService.getNeeds();
  method.subscribe(needs => this.needs = needs);
  }

   /**
   * Search for needs based off value
   */
  searchNeedsValue(): void{
    const valTerm = this.valTerm;
    if (this.valTerm === null || this.valTerm === undefined) {
    this.getNeeds();
    return;
  }
  const method = valTerm
  ? this.needsCupboardService.searchNeedsValue(this.valTerm)
  : this.needsCupboardService.getNeeds();
  method.subscribe(needs => this.needs = needs);

  }

  
  /**
   * add new need to backend
   * @param name 
   * 
   * @returns 
   */
  add(name: string): void {
    name = name.trim();
    if (!name) { return; }
    this.needsCupboardService.addNeed({ name } as Need)
      .subscribe(need => {
        this.needs.push(need);
      });
  }
  /**
   * Delete need within needs cupboard
   * @param need 
   */
  delete(need: Need): void {
    this.needs = this.needs.filter(h => h !== need);
    this.needsCupboardService.deleteNeed(need.id).subscribe();
  }
  /**
   * Move a need from the needs cupboard to the logged in users funding basket
   * @param need 
   */
  moveToFunding(need: Need): void{
    this.fundingBasketService.addNeed(this.userName,need).subscribe(() => {
      window.location.reload();
        this.needs.push(need);
      });
  }

  /**
   * 
   * @param amount Add donation from the needscupboard as a need to the funding basket
   */
  addDonation(amount: number): void {
    amount = Math.round(amount);
    if (amount <=0){
      alert("Cannot donate less than $1. Thankyou!")
    }
    else{
      const donationNeed: Need = {
        id: Math.floor(Math.random() * 1000000),    //random number 
        name: `Donation ($${amount})`,
        value: Number(amount),                       // donation amount
        maxQuantity: 1,
        currentQuantity: 0,
        type: "donation",
        visible: true
      };
      this.moveToFunding(donationNeed);
  } 
}

/**
 * Load the current values of totalDonated and donationGoal
 */
loadDonationStatus(): void {
  this.donationService.getStatus().subscribe(status => {
    this.totalDonated = status.totalDonated;
    this.donationGoal = status.donationGoal;
  });
}


}
