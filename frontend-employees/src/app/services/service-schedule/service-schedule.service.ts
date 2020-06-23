import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {ServiceScheduleDto} from '../dtos/Dtos';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {map} from 'rxjs/operators';

@Injectable({
    providedIn: 'root'
})
export class ServiceScheduleService {
    apiUrl = `${environment.backendApiRoot}/plan/standby`;

    constructor(private http: HttpClient) {
    }

    private mapToServiceSchedule = (schedules: any) => schedules.map(
        (entry: any) => {
            return {
                schedule: {
                    start: entry.startTime,
                    end: entry.endTime,

                },
                employee: entry.employee
            };
        })

    getServiceSchedule(): Observable<ServiceScheduleDto[]> {
        return this.http.get<any>(this.apiUrl)
            .pipe(
                map(this.mapToServiceSchedule),
            );
    }
}
