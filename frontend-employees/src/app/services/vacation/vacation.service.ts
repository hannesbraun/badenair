import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {RequestVacationDto, VacationPlanDto} from '../dtos/Dtos';
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

    requestVacation(dto: RequestVacationDto): Observable<void> {
        return this.http.post<void>(`${environment.backendApiRoot}/plan/vacation`, dto);
    }
}
