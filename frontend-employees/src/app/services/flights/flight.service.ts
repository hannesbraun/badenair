import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {PlaneScheduleDto} from '../dtos/Dtos';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Injectable({
    providedIn: 'root'
})
export class FlightService {

    apiUrl = environment.backendApiRoot;
    
    constructor(private http: HttpClient) {
    }

    getPlaneSchedules(): Observable<PlaneScheduleDto[]> {
        return this.http.get<PlaneScheduleDto[]>(`${environment.backendApiRoot}/flightplan`);
    }

    updateFlightTracking(flightId: number, action : string){
        return this.http.patch(`${this.apiUrl}/flight/tracking/${flightId}`, action);
        //Todo: Error-handling
    }
    
}
