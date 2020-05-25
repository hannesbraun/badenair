import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {ScheduleConflictDto} from '../dtos/Dtos';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Injectable({
    providedIn: 'root'
})
export class FlightService {

    apiUrl = environment.backendApiRoot;
    
    constructor(private http: HttpClient) {
    }

    getPlaneSchedules(): Observable<ScheduleConflictDto[]> {
        return this.http.get<ScheduleConflictDto[]>(`${environment.backendApiRoot}/flightplan/conflicts`);
    }
    
}