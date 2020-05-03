import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {AccountData, UpdateAccountDataDto} from '../dtos/Dtos';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class AccountService {

    constructor(private http: HttpClient) {
    }

    getAccountData(): Observable<AccountData> {
        return this.http.get<AccountData>(`${environment.backendApiRoot}/account`);
    }

    updateAccountData(dto: UpdateAccountDataDto): Observable<void> {
        return this.http.put<void>(`${environment.backendApiRoot}/account`, dto);
    }

}
