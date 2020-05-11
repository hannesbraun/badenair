import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { WorkingHoursDto } from '../dtos/Dtos';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class TimeTrackingService {

    constructor(private http: HttpClient) {
    }

    getLatestWorkingHours(): Observable<WorkingHoursDto> {
        return this.http.get<WorkingHoursDto>(`${environment.backendApiRoot}/workinghours`);
    }

    triggerWorkingHours(): Observable<WorkingHoursDto> {
        return this.http.post<WorkingHoursDto>(`${environment.backendApiRoot}/workinghours`, null);
    }
}
