import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowAccountComponent } from './show-account.component';

describe('ShowAccountComponent', () => {
  let component: ShowAccountComponent;
  let fixture: ComponentFixture<ShowAccountComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowAccountComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
