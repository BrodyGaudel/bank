import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RequestCodeComponent } from './request-code.component';

describe('RequestCodeComponent', () => {
  let component: RequestCodeComponent;
  let fixture: ComponentFixture<RequestCodeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RequestCodeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RequestCodeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
