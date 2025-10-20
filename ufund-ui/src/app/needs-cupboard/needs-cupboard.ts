import { Component, OnInit } from '@angular/core';
import {Need} from '../need';
import { NeedsCupboardService } from '../needs-cupboard-service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-needs-cupboard',
  standalone: false,
  templateUrl: './needs-cupboard.html',
  styleUrl: './needs-cupboard.css'
})
 export class NeedsCupboard implements OnInit{  
  constructor(private needsCupboardService: NeedsCupboardService) { }
  needs: Need[] = [];
  searchTerm = '';

  getNeeds(): void {
    const term = this.searchTerm.trim();
    const method = term
    ? this.needsCupboardService.searchNeeds(term)
    : this.needsCupboardService.getNeeds()
    method.subscribe(needs => this.needs = needs);
  }
 
  ngOnInit(): void {
    this.getNeeds();
  }

  add(name: string): void {
    name = name.trim();
    if (!name) { return; }
    this.needsCupboardService.addNeed({ name } as Need)
      .subscribe(need => {
        this.needs.push(need);
      });
  }

  delete(need: Need): void {
    this.needs = this.needs.filter(h => h !== need);
    this.needsCupboardService.deleteNeed(need.id).subscribe();
  }
  
}
