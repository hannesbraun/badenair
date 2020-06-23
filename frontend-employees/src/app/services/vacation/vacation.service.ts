import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {RequestVacationDto, VacationPlanDto, VacationRequestDeniedDto} from '../dtos/Dtos';
import {environment} from '../../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class VacationService {

    constructor(private http: HttpClient) {
    }

    getVacationPlan(): Observable<VacationPlanDto> {
        return this.http.get<VacationPlanDto>(`${environment.backendApiRoot}/plan/vacation`);
    }

    requestVacation(dto: RequestVacationDto): Observable<VacationRequestDeniedDto | void> {
        return this.http.post<VacationRequestDeniedDto | void>(`${environment.backendApiRoot}/plan/vacation`, dto);
    }
}
