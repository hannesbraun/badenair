import {Injectable} from '@angular/core';
import {Observable, of} from 'rxjs';
import {ScheduleConflictDto} from '../dtos/Dtos';

@Injectable({
    providedIn: 'root'
})
export class ScheduleConflictServiceService {

    constructor() {
    }

    getConflictForSchedule(scheduleId: number): Observable<ScheduleConflictDto> {
        // TODO: Get conflict from API
        return of({
            flight: {
                start: 'Lorem ipsum dolor sit amet',
                destination: 'Lorem ipsum dolor sit amet',
                startTime: new Date(),
                arrivalTime: new Date(),
            },
            cause: 'Flug wird sich aufgrund eines Sturms um 2 Stunden verzögern.'
        } as ScheduleConflictDto);
    }
}
