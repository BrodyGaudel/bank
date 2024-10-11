import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserPasswordUpdateComponent } from './user-password-update.component';

describe('UserPasswordUpdateComponent', () => {
  let component: UserPasswordUpdateComponent;
  let fixture: ComponentFixture<UserPasswordUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserPasswordUpdateComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserPasswordUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
