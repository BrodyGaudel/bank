import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccountGetComponent } from './account-get.component';

describe('AccountGetComponent', () => {
  let component: AccountGetComponent;
  let fixture: ComponentFixture<AccountGetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AccountGetComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AccountGetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
