import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowAllCustomerComponent } from './show-all-customer.component';

describe('ShowAllCustomerComponent', () => {
  let component: ShowAllCustomerComponent;
  let fixture: ComponentFixture<ShowAllCustomerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ShowAllCustomerComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ShowAllCustomerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
