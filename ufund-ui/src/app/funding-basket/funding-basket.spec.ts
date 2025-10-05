import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FundingBasket } from './funding-basket';

describe('FundingBasket', () => {
  let component: FundingBasket;
  let fixture: ComponentFixture<FundingBasket>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FundingBasket]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FundingBasket);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
