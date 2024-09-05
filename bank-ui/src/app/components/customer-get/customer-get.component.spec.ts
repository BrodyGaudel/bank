import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerGetComponent } from './customer-get.component';

describe('CustomerGetComponent', () => {
  let component: CustomerGetComponent;
  let fixture: ComponentFixture<CustomerGetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CustomerGetComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CustomerGetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
