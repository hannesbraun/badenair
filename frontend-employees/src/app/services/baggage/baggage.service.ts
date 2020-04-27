import {Injectable} from '@angular/core';
import {ChangeBaggageStateDto} from '../dtos/Dtos';
import {Observable} from 'rxjs';
import { HttpClient } from '@angular/common/http';
import {environment} from '../../../environments/environment'

@Injectable({
    providedIn: 'root'
})
export class BaggageService {

    constructor(private http: HttpClient) {
    }

    updateBaggageState(dto: ChangeBaggageStateDto): Observable<void> {
        return this.http.patch<void>(`${environment.backendApiRoot}/luggage`, dto);
    }
}
