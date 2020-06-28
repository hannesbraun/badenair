import {Injectable} from '@angular/core';
import {OAuthService} from 'angular-oauth2-oidc';
import {convertToDate} from '../services/util/DateUtil';

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    constructor(private oAuthService: OAuthService) {
    }

    login(returnPath?: string) {
        const url = returnPath ?? window.location.pathname;
        this.oAuthService.initCodeFlow(url);
    }

    loginWithState<T>(returnPath: string, dataKey: string, data: T) {
        this.storeData(dataKey, data);
        this.oAuthService.initCodeFlow(returnPath);
    }

    logout() {
        sessionStorage.clear();
        this.oAuthService.logOut();
    }

    isLoggedIn(): boolean {
        return this.oAuthService.hasValidIdToken();
    }

    getData<T>(dataKey: string): T {
        const item = sessionStorage.getItem(dataKey) ?? '{}';
        const parsedObject = JSON.parse(item);

        convertToDate(parsedObject);

        return parsedObject;
    }

    private storeData(dataKey: string, data: any) {
        sessionStorage.setItem(dataKey, JSON.stringify(data));
    }
}
