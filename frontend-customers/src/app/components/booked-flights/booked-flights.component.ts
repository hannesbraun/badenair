import {Component, OnInit} from '@angular/core';
import {FlightDto} from '../../services/dtos/Dtos';
import {Baggage, BaggageState, Person} from '../flight/flight.component';

interface BookedFlight {
    flights: FlightDto;
    persons: Person[];
    baggages: Baggage[];
}

@Component({
    selector: 'app-booked-flights',
    templateUrl: './booked-flights.component.html',
    styleUrls: ['./booked-flights.component.scss']
})
export class BookedFlightsComponent implements OnInit {

    bookedFlights: BookedFlight[] = [];

    constructor() {
    }

    ngOnInit() {
        // TODO: Replace with service
        for (let i = 0; i < 3; i++) {
            this.bookedFlights.push(
                {
                    flights:
                        {
                            id: i,
                            start: 'Lorem ipsum dolor sit amet',
                            destination: 'Lorem ipsum dolor sit amet',
                            startTime: new Date(),
                            arrivalTime: new Date(),
                        },
                    persons: [
                        {name: 'Peter Hase', id: 1},
                        {name: 'Klaus Kleber', id: 1},
                        {name: 'Max Mustermann', id: 1}
                    ],
                    baggages: [
                        {id: 9231, state: BaggageState.inPlane},
                        {id: 4782, state: BaggageState.onLoad}
                    ]
                }
            );
        }
    }
}
