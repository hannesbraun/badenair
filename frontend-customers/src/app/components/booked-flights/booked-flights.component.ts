import {Component, OnInit} from '@angular/core';
import {FlightDto} from "../../services/dtos/Dtos";

@Component({
    selector: 'app-booked-flights',
    templateUrl: './booked-flights.component.html',
    styleUrls: ['./booked-flights.component.scss']
})
export class BookedFlightsComponent implements OnInit {

    bookedFlights: FlightDto[] = [];

    constructor() {
    }

    ngOnInit() {
        // TODO: Replace with service
        for (let i = 0; i < 3; i++) {
            this.bookedFlights.push(
                {
                    id: i,
                    start: 'Lorem ipsum dolor sit amet',
                    destination: 'Lorem ipsum dolor sit amet',
                    startTime: new Date(),
                    arrivalTime: new Date(),
                }
            );
        }
    }
}
