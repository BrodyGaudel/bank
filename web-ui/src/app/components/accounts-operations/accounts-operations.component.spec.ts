import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccountsOperationsComponent } from './accounts-operations.component';

describe('AccountsOperationsComponent', () => {
  let component: AccountsOperationsComponent;
  let fixture: ComponentFixture<AccountsOperationsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AccountsOperationsComponent]
    });
    fixture = TestBed.createComponent(AccountsOperationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
