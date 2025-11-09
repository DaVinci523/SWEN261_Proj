import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewsPage } from './review-page';

describe('ReviewsPage', () => {
  let component: ReviewsPage;
  let fixture: ComponentFixture<ReviewsPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReviewsPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReviewsPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
