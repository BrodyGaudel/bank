import { TestBed } from '@angular/core/testing';

import { OperationService } from './operation.service';

describe('OperationService', () => {
  let service: OperationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OperationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
