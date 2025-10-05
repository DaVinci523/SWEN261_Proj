import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HelperPage } from './helper-page';

describe('HelperPage', () => {
  let component: HelperPage;
  let fixture: ComponentFixture<HelperPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [HelperPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HelperPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
