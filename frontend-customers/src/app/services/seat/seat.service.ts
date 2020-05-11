import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {SeatDto} from '../dtos/Dtos';
import {environment} from '../../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class SeatService {

    baseUrl = `${environment.backendApiRoot}/seat`;

    constructor(private http: HttpClient) {
    }

    getSeats(id: number): Observable<SeatDto> {
        const data: any = {
            flightId: id
        };
        return this.http.get<SeatDto>(this.baseUrl, {params: data});
    }
}
