import {Component, OnInit} from '@angular/core';
import {FlightService} from '../../services/flight/flight.service';
import {Observable} from 'rxjs';
import {AirportDto} from '../../services/dtos/Dtos';
import {AirportService} from '../../services/airport/airport.service';
import {Router} from '@angular/router';
import {SearchService} from '../../services/search/search.service';

@Component({
    selector: 'app-flight-search-page',
    templateUrl: './flight-search-page.component.html',
    styleUrls: ['./flight-search-page.component.scss']
})
export class FlightSearchPageComponent implements OnInit {
    airports$ = new Observable<AirportDto[]>();

    args = {
        date: new Date().toISOString(),
        start: 0,
        destination: 1,
        passengers: 3,
    };

    constructor(
        private flightService: FlightService,
        private airportService: AirportService,
        private router: Router,
        private searchService: SearchService,
    ) {
    }

    ngOnInit(): void {
        this.airports$ = this.airportService.getAirports();
    }

    search(value: any) {
        console.log(value);
        if (value.type === '1') {
            const flightRequest1 = {
                start: value.start,
                destination: value.destination,
                passengers: value.passengers,
                date: value.fromDate.toISOString(),
            };
            const flightRequest2 = {
                destination: value.start,
                start: value.destination,
                passengers: value.passengers,
                date: value.toDate.toISOString(),
            };
            this.flightService.searchFlights(flightRequest1)
                .subscribe(data => {
                    this.searchService.setFlightSearchData(data);
                    this.router.navigate(['/flights']);
                });

            this.flightService.searchFlights(flightRequest2)
                .subscribe(data => console.log(data));
        }
    }
}
