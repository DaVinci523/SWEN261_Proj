import { TestBed } from '@angular/core/testing';

import { NeedsCupboardService } from './needs-cupboard';

describe('NeedsCupboard', () => {
  let service: NeedsCupboardService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NeedsCupboardService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
