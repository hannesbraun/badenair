import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {BookedFlightsComponent} from './booked-flights.component';

describe('FlightOverviewComponent', () => {
    let component: BookedFlightsComponent;
    let fixture: ComponentFixture<BookedFlightsComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [BookedFlightsComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(BookedFlightsComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
