import { TestBed } from '@angular/core/testing';

import { TokenServiceService } from './token-service.service';

describe('TokenServceService', () => {
  let service: TokenServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TokenServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
