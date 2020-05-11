import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PassengersPageComponent } from './passengers-page.component';

describe('PassengersPageComponent', () => {
  let component: PassengersPageComponent;
  let fixture: ComponentFixture<PassengersPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PassengersPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PassengersPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
