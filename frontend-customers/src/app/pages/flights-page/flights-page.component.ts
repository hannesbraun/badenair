import {Component, OnInit} from '@angular/core';
import {FlightDto} from '../../services/dtos/Dtos';
import {BookingStateService} from '../../services/search/booking-state.service';
import {Router} from '@angular/router';


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
    directionState = true;
    type = '1';

    constructor(
        private bookingStateService: BookingStateService,
        private router: Router,
    ) {
    }

    ngOnInit() {
        this.bookingStateService.state
            .subscribe(data => {
                this.toFlights = data.toFlights;
                this.returnFlights = data.returnFlights;
                this.type = data.searchValue.type;
                this.directionState = data.direction;
                this.numberOfPassengers = data.passengers;

                if (!this.directionState) {
                    this.shownFlights = data.toFlights;
                } else {
                    this.shownFlights = data.returnFlights;
                }
            });
    }

    onBookingStateChanged(flight: FlightDto) {
        if (this.directionState) {
            this.bookingStateService.setSelectedToFlight(flight);
        } else {
            this.bookingStateService.setSelectedReturnFlight(flight);
        }
        this.next();
    }

    next() {
        if (this.type === '2') {
            this.router.navigate(['/passengers']);
            return;
        }
        if (this.directionState) {
            this.directionState = false;
            this.bookingStateService.setDirection(false);
            this.shownFlights = this.returnFlights;
        } else {
            this.router.navigate(['/passengers']);
        }
    }

    previous() {
        if (this.type === '2') {
            this.router.navigate(['/']);
            return;
        }
        if (this.directionState) {
            this.router.navigate(['/']);
        } else {
            this.directionState = true;
            this.bookingStateService.setDirection(true);
            this.shownFlights = this.toFlights;
        }
    }
}
