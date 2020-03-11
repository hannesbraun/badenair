import {Injectable} from '@angular/core';
import {Observable, of} from 'rxjs';
import {Schedule} from '../dtos/Dtos';

@Injectable({
    providedIn: 'root'
})
export class ShiftScheduleService {

    constructor() {
    }

    getScheduleForEmployee(id: number): Observable<Schedule[]> {
        // TODO: Replace with API call
        return of(
            [
                {
                    start: new Date(),
                    end: new Date()
                },
                {
                    start: new Date(),
                    end: new Date()
                },
                {
                    start: new Date(),
                    end: new Date()
                },
                {
                    start: new Date(),
                    end: new Date()
                },
                {
                    start: new Date(),
                    end: new Date()
                }
            ]
        );
    }
}
