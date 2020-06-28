import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {ScheduleConflictDto, ReservePlaneSolutionDto} from '../dtos/Dtos';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Injectable({
    providedIn: 'root'
})
export class ConflictService {

    apiUrl = environment.backendApiRoot;
    
    constructor(private http: HttpClient) {
    }

    getConflicts(): Observable<ScheduleConflictDto[]> {
        return this.http.get<ScheduleConflictDto[]>(`${environment.backendApiRoot}/flightplan/conflicts`);
    }
    
    useReservePlane(data: ReservePlaneSolutionDto): Observable<void>{
        return this.http.post<void>(`${environment.backendApiRoot}/flightplan/conflicts/useReservePlane`, data);
    }

    cancelFlight(flightID: number): Observable<void>{
        return this.http.post<void>(`${environment.backendApiRoot}/flightplan/conflicts/cancelFlight/${flightID}`, null);
    }

    ignoreDelay(flightID: number): Observable<void>{
        return this.http.post<void>(`${environment.backendApiRoot}/flightplan/conflicts/ignoreDelay/${flightID}`, null);
    }
}