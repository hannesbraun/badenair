import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StartLandingComponent } from './start-landing.component';

describe('StartLandingComponent', () => {
  let component: StartLandingComponent;
  let fixture: ComponentFixture<StartLandingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StartLandingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StartLandingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
