import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewSection } from './review-page';

describe('ReviewSection', () => {
  let component: ReviewSection;
  let fixture: ComponentFixture<ReviewSection>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReviewSection]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReviewSection);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
