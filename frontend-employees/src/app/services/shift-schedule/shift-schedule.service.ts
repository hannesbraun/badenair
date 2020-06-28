import {Injectable} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from 'rxjs';
import { environment } from '../../../environments/environment';
import { Schedule } from '../dtos/Dtos';


@Injectable({
    providedIn: 'root'
})

export class ShiftScheduleService {

    apiUrl = environment.backendApiRoot;

    constructor(private http : HttpClient) { }

    getScheduleForEmployee(): Observable<Schedule[]> {
        return this.http.get<Schedule[]>(`${this.apiUrl}/plan/shift`);
    }
}
