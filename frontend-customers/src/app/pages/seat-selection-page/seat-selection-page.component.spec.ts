import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SeatSelectionPageComponent } from './seat-selection-page.component';

describe('SeatSelectionPageComponent', () => {
  let component: SeatSelectionPageComponent;
  let fixture: ComponentFixture<SeatSelectionPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SeatSelectionPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SeatSelectionPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
