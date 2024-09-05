import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerAllComponent } from './customer-all.component';

describe('CustomerAllComponent', () => {
  let component: CustomerAllComponent;
  let fixture: ComponentFixture<CustomerAllComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CustomerAllComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CustomerAllComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
