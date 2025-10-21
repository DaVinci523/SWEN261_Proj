import { Component } from '@angular/core';
import { AdminPageService } from '../admin-page-service'; 
import { Need } from '../need';
import { User } from '../user';

@Component({
  selector: 'app-admin-page',
  standalone: false,
  templateUrl: './admin-page.html',
  styleUrl: './admin-page.css'
})
export class AdminPage {

  constructor(private adminPageService:AdminPageService, public user: User){}  
  needs:Need[] = [];
  
  getNeeds(): void {
    this.adminPageService.getNeeds().subscribe(needs => this.needs = needs);
  }

  createNeed(name: string, value: number, quantity: number, type: string): void {
    name = name.trim();
    if (!name) { 
      return; 
    }
    console.log(value, quantity);
    this.adminPageService.createNeed({ name, value, quantity, type } as Need)
      .subscribe(need => {
        this.needs.push(need);
      });
    }

  deleteNeed(need: Need): void { 
    this.needs = this.needs.filter(n => n !== need);
    this.adminPageService.deleteNeedID(need.id).subscribe( needs => 
      this.needs = this.needs.filter(n => n !== needs));
  }

  updateNeed(need: Need): void {
    this.adminPageService.updateNeed(need).subscribe();
  }

  ngOnInit(): void {
    this.getNeeds();
  }

  

  

}
