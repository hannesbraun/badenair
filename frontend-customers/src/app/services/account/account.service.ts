import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {AccountData} from '../dtos/Dtos';
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
}
