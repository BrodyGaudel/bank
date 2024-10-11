import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserResetPasswordComponent } from './user-reset-password.component';

describe('UserResetPasswordComponent', () => {
  let component: UserResetPasswordComponent;
  let fixture: ComponentFixture<UserResetPasswordComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserResetPasswordComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserResetPasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
