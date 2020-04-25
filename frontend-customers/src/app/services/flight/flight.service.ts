import {Injectable} from '@angular/core';
import {Observable, of} from 'rxjs';
import {FlightDto} from '../dtos/Dtos';
import {environment} from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {map} from 'rxjs/operators';

@Injectable({
    providedIn: 'root'
})
export class FlightService {
    baseUrl = `${environment.backendApiRoot}/public/flight/search`;

    constructor(private http: HttpClient) {
    }

    searchFlights(data: any): Observable<FlightDto[]> {
        return this.http.get<FlightDto[]>(this.baseUrl, {params:  data})
            .pipe(
                map(flights =>
                    flights.map(flight => {
                            return {
                                ...flight,
                                arrivalTime: new Date(flight.arrivalTime),
                                startTime: new Date(flight.startTime),
                            };
                        }
                    )
                )
            );
    }

    getFlight(id: number): Observable<FlightDto> {
        return of(
            {
                id: 1,
                start: 'Lorem ipsum dolor sit amet',
                destination: 'Lorem ipsum dolor sit amet',
                startTime: new Date(),
                arrivalTime: new Date(),
                price: 200
            }
        );
    }
}
