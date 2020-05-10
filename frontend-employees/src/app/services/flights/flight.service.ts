import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {PlaneScheduleDto} from '../dtos/Dtos';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Injectable({
    providedIn: 'root'
})
export class FlightService {

    constructor(private http: HttpClient) {
    }

    getPlaneSchedules(): Observable<PlaneScheduleDto[]> {
        return this.http.get<PlaneScheduleDto[]>(`${environment.backendApiRoot}/flightplan`);
    }
}
