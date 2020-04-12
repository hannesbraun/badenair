import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {RequestVacationDto, VacationDto} from '../dtos/Dtos';
import {environment} from '../../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class VacationService {

    constructor(private http: HttpClient) {
    }

    getVacationPlan(): Observable<VacationDto[]> {
        return this.http.get<VacationDto[]>(`${environment.backendApiRoot}/plan/vacation`);
    }

    requestVacation(dto: RequestVacationDto): Observable<void> {
        return this.http.post<void>(`${environment.backendApiRoot}/plan/vacation`, dto);
    }
}
