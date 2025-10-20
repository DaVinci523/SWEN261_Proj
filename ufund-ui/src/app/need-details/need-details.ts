import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { Need } from '../need';
import { NeedsCupboardService } from '../needs-cupboard-service';

@Component({
  selector: 'app-need-details',
  templateUrl: './need-details.html',
  styleUrls: [ './need-details.css' ],
  standalone:false
})
export class NeedDetailComponent implements OnInit {
  need: Need | undefined;

  constructor(
    private route: ActivatedRoute,
    private needsCupboardService: NeedsCupboardService,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.getNeed();
  }

  getNeed(): void {
    const id = parseInt(this.route.snapshot.paramMap.get('id')!, 10);
    this.needsCupboardService.getNeed(id)
      .subscribe(need => this.need = need);
  }
  goBack(): void {
    this.location.back();
  }

  save(): void {
    if (this.need) {
      this.needsCupboardService.updateNeed(this.need)
        .subscribe(() => this.goBack());
    }
  }
}

function goBack() {
  throw new Error('Function not implemented.');
}
function save() {
  throw new Error('Function not implemented.');
}

