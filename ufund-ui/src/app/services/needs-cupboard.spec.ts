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

  it('should fetch a need by ID', () => {
    service.getNeedsByID(1).subscribe(need => expect(need).toEqual(testNeeds[0]));
    const req = httpMock.expectOne(`${service['needsUrl']}/1`);
    expect(req.request.method).toBe('GET');
    req.flush(testNeeds[0]);
  })

  it('should create a new need', () => {
    const newNeed: Need = { id: 3, name: 'Cords', value: 30, quantity: 5, type: 'Equipment' }
    service.createNeed(newNeed).subscribe(need => expect(need).toEqual(newNeed));
    const req = httpMock.expectOne(service['needsUrl']);
    expect(req.request.method).toBe('POST');
    req.flush(newNeed);
  })

  it('should update an existing need', () => {
    const updatedNeed: Need = { ...testNeeds[0], quantity: 5};
    service.updateNeed(updatedNeed).subscribe(need => expect(need.quantity).toBe(5));
    const req = httpMock.expectOne(service['needsUrl']);
    expect(req.request.method).toBe('PUT');
    req.flush(updatedNeed);
  })

  it('should search needs by name', () => {
    service.searchNeeds('Microscope').subscribe(needs => {
      expect(needs.length).toBe(1);
      expect(needs[0].name).toBe('Microscope');
    });
    
    const req = httpMock.expectOne(`${service['needsUrl']}/?name=Microscope`);
    expect(req.request.method).toBe('GET');
    req.flush([testNeeds[0]]);
  });

  it('should delete a need by ID', () => {
    service.deleteNeedById(1).subscribe(response => expect(response).toBeUndefined());

    const req = httpMock.expectOne(`${service['needsUrl']}/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

  it('should handle HTTP errors gracefully', () => {
    service.getNeeds().subscribe(needs => expect(needs).toEqual([]));

    const req = httpMock.expectOne(service['needsUrl']);
    req.flush('Error occured', {status: 500, statusText: 'Server Error '});
  })
});
