import {Injectable} from '@angular/core';
import {Observable, of} from 'rxjs';
import {FlightDto} from '../dtos/Dtos';

@Injectable({
    providedIn: 'root'
})
export class FlightService {

    constructor() {
    }

    getFlight(id: number): Observable<FlightDto> {
        return of(
            {
                id: 1,
                start: 'Lorem ipsum dolor sit amet',
                destination: 'Lorem ipsum dolor sit amet',
                startTime: new Date(),
                arrivalTime: new Date(),
            }
        );
    }
}
