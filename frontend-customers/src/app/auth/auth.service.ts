import {Injectable} from '@angular/core';
import {OAuthService} from 'angular-oauth2-oidc';

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    constructor(private oAuthService: OAuthService) {
    }

    login() {
        this.oAuthService.initCodeFlow();
    }

    logout() {
        this.oAuthService.logOut();
    }

    isLoggedIn(): boolean {
        return this.oAuthService.hasValidIdToken();
    }

    getGivenName() {
        return (this.oAuthService.getIdentityClaims() as {given_name: string}).given_name;
    }

    getFamilyName() {
        return (this.oAuthService.getIdentityClaims() as {family_name: string}).family_name;
    }
}
