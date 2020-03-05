import {Injectable} from '@angular/core';
import {Observable, of} from 'rxjs';
import {ServiceScheduleDto} from '../dtos/Dtos';

@Injectable({
    providedIn: 'root'
})
export class ServiceScheduleService {

    constructor() {
    }

    getServiceSchedule(): Observable<ServiceScheduleDto[]> {
        // TODO: Replace with API call
        return of([
            {
                schedule: {
                    start: new Date(),
                    end: new Date()
                },
                employee: 'Max Mustermann'
            },
            {
                schedule: {
                    start: new Date(),
                    end: new Date()
                },
                employee: 'Max Mustermann'
            },
            {
                schedule: {
                    start: new Date(),
                    end: new Date()
                },
                employee: 'Max Mustermann'
            },
            {
                schedule: {
                    start: new Date(),
                    end: new Date()
                },
                employee: 'Max Mustermann'
            }
        ]);
    }
}
