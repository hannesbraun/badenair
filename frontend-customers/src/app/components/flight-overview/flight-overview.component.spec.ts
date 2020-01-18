import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FlightOverviewComponent } from './flight-overview.component';

describe('FlightOverviewComponent', () => {
  let component: FlightOverviewComponent;
  let fixture: ComponentFixture<FlightOverviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FlightOverviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FlightOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
