import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {ScheduleConflictDto} from '../dtos/Dtos';
import { HttpClient } from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class ScheduleConflictService {

    private _url: string = "/assets/test_conflict.json";

    constructor(private http:HttpClient) {
    }

    getConflicts(): Observable<ScheduleConflictDto[]> {
        // TODO: Get conflict from API
        

        return this.http.get<ScheduleConflictDto[]>(this._url);
    }
}
