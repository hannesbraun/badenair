import {Component, OnInit} from '@angular/core';
import {FlightDto} from '../../services/dtos/Dtos';
import {BookingStateService} from '../../services/search/booking-state.service';
import {Router} from '@angular/router';
import {AuthService} from '../../auth/auth.service';


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
        private router: Router, private authService: AuthService
    ) {
    }

    ngOnInit() {
        this.bookingStateService.state
            .subscribe(data => {
                this.toFlights = data.toFlights;
                this.returnFlights = data.returnFlights;
                this.type = data.searchValue.type;
                this.numberOfPassengers = data.passengers;

                if (this.directionState !== undefined) {
                    this.directionState = data.direction;
                }

                if (this.directionState) {
                    if (data.toFlights) {
                        this.shownFlights = data.toFlights;
                    }
                } else {
                    if (data.returnFlights) {
                        this.shownFlights = data.returnFlights;
                    }
                }
            },
            err => {console.error("Ein unerwarteter Fehler ist aufgetreten")},
            ()=> {
                this.shownFlights.sort((a, b) => a.startTime.getTime()- b.startTime.getTime());
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
            this.authService.loginWithState('/passengers', BookingStateService.BOOKING_STATE_KEY,
                this.bookingStateService.state.getValue());
            return;
        }
        if (this.directionState) {
            this.directionState = false;
            this.bookingStateService.setDirection(false);
            this.shownFlights = this.returnFlights;
        } else {
            this.authService.loginWithState('/passengers', BookingStateService.BOOKING_STATE_KEY,
                this.bookingStateService.state.getValue());
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
