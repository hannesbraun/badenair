import {Component, OnInit} from '@angular/core';
import {FlightDto} from '../../services/dtos/Dtos';
import {BookingState} from '../../components/flight/check-button/check-button.component';
import {BookingStateService} from '../../services/search/booking-state.service';


@Component({
    selector: 'app-flights-page',
    templateUrl: './flights-page.component.html',
    styleUrls: ['./flights-page.component.scss']
})
export class FlightsPageComponent implements OnInit {

    shownFlights: FlightDto[] = [];
    returnFlights: FlightDto[] = [];
    toFlights: FlightDto[] = [];
    numberOfPassengers = 3;

    private bookedFlights: number[] = [];

    constructor(private searchService: BookingStateService) {
    }

    ngOnInit() {
        this.searchService.state
            .subscribe(data => {
                this.shownFlights = data.toFlights;
                this.shownFlights = data.returnFlights;
                this.numberOfPassengers = data.passengers;
            });
    }

    onBookingStateChanged(newState: BookingState, flightId: number) {
        if (newState === BookingState.BOOKED) {
            this.bookedFlights.push(flightId);
        } else {
            this.bookedFlights = this.bookedFlights.filter(id => id !== flightId);
        }
    }

    bookedFlightsEmpty(): boolean {
        return this.bookedFlights.length === 0;
    }
}
