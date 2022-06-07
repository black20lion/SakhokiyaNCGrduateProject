import { TestBed } from '@angular/core/testing';

import { ProductCardServiceService } from './product-card-service.service';

describe('ProductCardServiceService', () => {
  let service: ProductCardServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProductCardServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
