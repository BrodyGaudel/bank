import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowCustomerComponent } from './show-customer.component';

describe('ShowCustomerComponent', () => {
  let component: ShowCustomerComponent;
  let fixture: ComponentFixture<ShowCustomerComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ShowCustomerComponent]
    });
    fixture = TestBed.createComponent(ShowCustomerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
