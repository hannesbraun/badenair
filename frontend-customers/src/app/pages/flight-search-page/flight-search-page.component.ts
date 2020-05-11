import {Component, OnInit} from '@angular/core';
import {FlightService} from '../../services/flight/flight.service';
import {Observable} from 'rxjs';
import {AirportDto} from '../../services/dtos/Dtos';
import {AirportService} from '../../services/airport/airport.service';
import {Router} from '@angular/router';
import {BookingStateService} from '../../services/search/booking-state.service';

@Component({
    selector: 'app-flight-search-page',
    templateUrl: './flight-search-page.component.html',
    styleUrls: ['./flight-search-page.component.scss']
})
export class FlightSearchPageComponent implements OnInit {
    airports$ = new Observable<AirportDto[]>();
    searchValue: any;

    constructor(
        private flightService: FlightService,
        private airportService: AirportService,
        private router: Router,
        private bookingStateService: BookingStateService,
    ) {
    }

    ngOnInit(): void {
        this.airports$ = this.airportService.getAirports();
        this.bookingStateService.state
            .subscribe(bookingState => this.searchValue = bookingState.searchValue);
    }

    search(value: any) {
        if (value.type === '1') {
            const flightRequest1 = {
                start: value.start,
                destination: value.destination,
                passengers: value.passengers,
                date: this.toISOStringWithTimezone(value.fromDate),
            };
            const flightRequest2 = {
                destination: value.start,
                start: value.destination,
                passengers: value.passengers,
                date: this.toISOStringWithTimezone(value.toDate),
            };
            this.flightService.searchFlights(flightRequest1)
                .subscribe(flights => {
                    this.bookingStateService.setToFlights(flights);
                });

            this.flightService.searchFlights(flightRequest2)
                .subscribe(flights => {
                    this.bookingStateService.setReturnFlights(flights);
                });

        } else {
            const flightRequest1 = {
                start: value.start,
                destination: value.destination,
                passengers: value.passengers,
                date: this.toISOStringWithTimezone(value.fromDate)
            };
            this.flightService.searchFlights(flightRequest1)
                .subscribe(flights => {
                    this.bookingStateService.setToFlights(flights);
                });
        }
        this.bookingStateService.setSearchValue(value);
        this.bookingStateService.setPassengers(value.passengers);
        this.router.navigate(['/flights']);
    }

    private toISOStringWithTimezone(date: Date): string {
        const timezoneOffsetInMilliseconds = (new Date()).getTimezoneOffset() * 60000;
        return (new Date(date.getTime() - timezoneOffsetInMilliseconds)).toISOString();
    }
}
