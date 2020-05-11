import {Component, OnInit} from '@angular/core';
import {FlightDto} from '../../services/dtos/Dtos';
import {Baggage, Person} from '../flight/flight.component';
import { AccountService } from 'src/app/services/account/account.service';

export interface BookedFlight {
    flight: FlightDto;
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

    constructor(private accountService: AccountService) {
    }

    ngOnInit() {
        this.accountService.getBookings().subscribe((data: BookedFlight[]) => this.bookedFlights = {...data});
    }

    checkInPossible(bookedFlight: BookedFlight): boolean {
        var possible = false;

        // Check in only possible until 30 minutes before departure
        if (bookedFlight.flight.startTime > new Date(Date.now() + 30 * 60000)) {
            // Check in only possible if at least one person is not checked in yet
            for (let person of bookedFlight.persons) {
                if (!person.checkedIn) {
                    possible = true;
                }
            }
        }

        return possible;
    }
}
