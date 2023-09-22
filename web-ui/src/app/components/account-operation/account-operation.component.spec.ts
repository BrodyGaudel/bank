import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccountOperationComponent } from './account-operation.component';

describe('AccountOperationComponent', () => {
  let component: AccountOperationComponent;
  let fixture: ComponentFixture<AccountOperationComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AccountOperationComponent]
    });
    fixture = TestBed.createComponent(AccountOperationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
