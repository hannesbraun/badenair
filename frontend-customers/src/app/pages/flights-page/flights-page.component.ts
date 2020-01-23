import {Component, OnInit} from '@angular/core';
import {FlightDto} from '../../services/dtos/Dtos';
import {BookingState} from '../../components/flight/check-button/check-button.component';

interface FlightWithPrice {
    flight: FlightDto;
    price: number;
}

@Component({
    selector: 'app-flights-page',
    templateUrl: './flights-page.component.html',
    styleUrls: ['./flights-page.component.scss']
})
export class FlightsPageComponent implements OnInit {

    flightsWithPrice: FlightWithPrice[] = [];
    numberOfPassengers = 3;

    private bookedFlights: number[] = [];

    constructor() {
    }

    ngOnInit() {
        for (let i = 0; i < 10; i++) {
            this.flightsWithPrice.push(
                {
                    flight: {
                        id: i,
                        start: 'Lorem ipsum dolor sit amet',
                        destination: 'Lorem ipsum dolor sit amet',
                        startTime: new Date(),
                        arrivalTime: new Date(),
                    },
                    price: 50 + Math.random() * 100 + Math.random()
                }
            );
        }
    }

    onBookingStateChanged(newState: BookingState, flightId: number) {
        if (newState === BookingState.BOOKED) {
            this.bookedFlights.push(flightId);
        } else {
            this.bookedFlights = this.bookedFlights.filter(id => id !== flightId);
        }
        console.log(this.bookedFlights);
    }
}
