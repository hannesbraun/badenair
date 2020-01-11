import {Injectable} from '@angular/core';
import {Observable, of} from 'rxjs';
import {PlaneScheduleDto} from '../dtos/Dtos';

@Injectable({
    providedIn: 'root'
})
export class FlightService {

    constructor() {
    }

    // TODO: Remove when getPlaneSchedules is implemented
    private static getStartDate(): Date {
        const date = new Date();
        date.setHours(date.getHours() + (Math.random() * 10));
        date.setMinutes(Math.random() * 60);
        return date;
    }

    // TODO: Remove when getPlaneSchedules is implemented
    private static getArrivalDate(d: Date): Date {
        const date = new Date();
        date.setHours(d.getHours() + 1 + (Math.random() * 10));
        date.setMinutes(Math.random() * 60);
        return date;
    }

    // TODO: Remove when getPlaneSchedules is implemented
    private static getSchedules(i: number): PlaneScheduleDto {
        const startDate = FlightService.getStartDate();
        const arrivalDate = FlightService.getArrivalDate(startDate);

        return {
            id: i,
            plane: 'Plane ' + i,
            status: 'OK',
            hasConflict: false,
            flights: [
                {
                    start: 'Lorem ipsum dolor sit amet',
                    destination: 'Lorem ipsum dolor sit amet',
                    startTime: startDate,
                    arrivalTime: arrivalDate,
                }
            ]
        } as PlaneScheduleDto;
    }

    getPlaneSchedules(): Observable<PlaneScheduleDto[]> {
        // TODO: Get flights from API
        const flights: PlaneScheduleDto[] = [];

        for (let i = 0; i < 10; i++) {
            flights.push(FlightService.getSchedules(i));
        }

        return of(flights);
    }
}
