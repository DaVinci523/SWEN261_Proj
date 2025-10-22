import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { Need } from '../need';
import { NeedsCupboardService } from '../needs-cupboard-service';
import { FundingBasketService } from '../funding-basket-service';
import { User } from '../user';

@Component({
  selector: 'app-needs-cupboard',
  standalone: false,
  templateUrl: './needs-cupboard.html',
  styleUrls: ['./needs-cupboard.css']
})
export class NeedsCupboard implements OnInit {
  constructor(
    private needsCupboardService: NeedsCupboardService,
    private fundingBasketService: FundingBasketService,
    private router: Router
  ) {}

  user = inject(User);

  userName: string = '';
  needs: Need[] = [];
  searchTerm = '';
  buttonText: string = 'Add to Cart';
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

    const saved = localStorage.getItem('buttonText');
    if (saved) this.buttonText = saved;
  }

  /**
   * Gathers full list of needs in needs cupboard JSON
   */
  getNeeds(): void {
    const term = this.searchTerm.trim();
    const method = term
    ? this.needsCupboardService.searchNeeds(term)
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

}
