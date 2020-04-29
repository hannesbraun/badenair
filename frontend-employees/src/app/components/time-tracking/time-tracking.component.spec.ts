import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TimeTrackingComponent } from './time-tracking.component';
import { HttpClientModule } from '@angular/common/http';

describe('TimeTrackingComponent', () => {
    let component: TimeTrackingComponent;
    let fixture: ComponentFixture<TimeTrackingComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [HttpClientModule],
            declarations: [TimeTrackingComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(TimeTrackingComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
