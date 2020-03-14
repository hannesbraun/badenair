import {Injectable} from '@angular/core';
import {Observable, of} from 'rxjs';
import {PassengerDto} from '../dtos/Dtos';

@Injectable({
    providedIn: 'root'
})
export class PassengerService {

    constructor() {
    }

    getPassengersForFlight(id: number): Observable<PassengerDto[]> {
        return of(
            [
                {
                    name: 'Max',
                    surname: 'Mustermann',
                    checkedIn: false,
                    baggage1: 20,
                    baggage2: 0,
                    baggage3: 0,
                    baggage4: 0
                },
                {
                    name: 'Bob',
                    surname: 'Ross',
                    checkedIn: false,
                    baggage1: 20,
                    baggage2: 30,
                    baggage3: 0,
                    baggage4: 0
                }
            ]
        );
    }
}
