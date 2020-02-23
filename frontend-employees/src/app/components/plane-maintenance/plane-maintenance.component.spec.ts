import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PlaneMaintenanceComponent } from './plane-maintenance.component';

describe('PlaneMaintenanceComponent', () => {
  let component: PlaneMaintenanceComponent;
  let fixture: ComponentFixture<PlaneMaintenanceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PlaneMaintenanceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PlaneMaintenanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
