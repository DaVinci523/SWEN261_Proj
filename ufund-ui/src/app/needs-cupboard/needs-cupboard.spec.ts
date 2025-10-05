import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NeedsCupboard } from './needs-cupboard';

describe('NeedsCupboard', () => {
  let component: NeedsCupboard;
  let fixture: ComponentFixture<NeedsCupboard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NeedsCupboard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NeedsCupboard);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
