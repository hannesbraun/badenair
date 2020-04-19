import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {AirportDto} from '../dtos/Dtos';

@Injectable({
    providedIn: 'root'
})
export class AirportService {
    baseUrl = `${environment.backendApiRoot}/airport`;

    constructor(private http: HttpClient) {
    }

    getAirports(): Observable<AirportDto[]> {
        return this.http.get<AirportDto[]>(this.baseUrl);
    }
}
