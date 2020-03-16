import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BookingOverviewPageComponent } from './booking-overview-page.component';

describe('BookingOverviewPageComponent', () => {
  let component: BookingOverviewPageComponent;
  let fixture: ComponentFixture<BookingOverviewPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BookingOverviewPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BookingOverviewPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
