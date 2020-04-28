import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {PlaneScheduleDto} from '../dtos/Dtos';
import { HttpClient } from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class FlightService {

    private _url: string = "/assets/test.json";

    constructor(private http:HttpClient) {
    }

    getPlaneSchedules(): Observable<PlaneScheduleDto[]> {
        return this.http.get<PlaneScheduleDto[]>(this._url);
    }
}
