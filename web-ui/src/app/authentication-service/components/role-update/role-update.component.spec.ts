import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoleUpdateComponent } from './role-update.component';

describe('RoleUpdateComponent', () => {
  let component: RoleUpdateComponent;
  let fixture: ComponentFixture<RoleUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RoleUpdateComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RoleUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
