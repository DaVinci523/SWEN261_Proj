import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { NeedsCupboardService } from './needs-cupboard';
import { Need } from '../need';
import { HttpClient } from '@angular/common/http';

describe('NeedsCupboardService', () => {
  let service: NeedsCupboardService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [NeedsCupboardService]
    });
    service = TestBed.inject(NeedsCupboardService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  })

  const testNeeds: Need[] = [
    { id: 1, name: 'Microscope', value: 500, quantity: 2, type: 'Equipment'},
    { id: 2, name: 'Pipette', value: 100, quantity: 10, type: 'Equipment' },
  ];

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch all needs', () => {
    service.getNeeds().subscribe(needs => expect(needs).toEqual(testNeeds));
    const req = httpMock.expectOne(service['needsUrl']);
    expect(req.request.method).toBe('GET');
    req.flush(testNeeds);
  });

  
});
