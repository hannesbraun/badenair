import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PlaneScheduleDto, TrackingDto, FlightDto } from '../dtos/Dtos';
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

    updateFlightTracking(flightId: number, dto: TrackingDto) {
        return this.http.patch(`${this.apiUrl}/flight/tracking/${flightId}`, dto);
        //Todo: Error-handling
    }

    getCurrentTrackingAction(flightId: number): Observable<TrackingDto> {
        return this.http.get<TrackingDto>(`${this.apiUrl}/flight/tracking/action/${flightId}`);
    }

    getCurrentFlightforPilot(): Observable<FlightDto> {
        return this.http.get<FlightDto>(`${this.apiUrl}/flight/pilot`);
    }

    getNextFlights(): Observable<FlightDto[]> {
        return this.http.get<FlightDto[]>(`${this.apiUrl}/flight/crewplan`);
    }

    setMaintenance(flightId: number): Observable<void> {
        return this.http.patch<void>(`${this.apiUrl}/flight/maintenance/${flightId}`, null);
    }
}
