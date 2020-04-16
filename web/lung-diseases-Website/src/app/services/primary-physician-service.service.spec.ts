import { TestBed } from '@angular/core/testing';

import { PrimaryPhysicianServiceService } from './primary-physician-service.service';

describe('PrimaryPhysicianServiceService', () => {
  let service: PrimaryPhysicianServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PrimaryPhysicianServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
