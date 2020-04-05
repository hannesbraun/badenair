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
        this.oAuthService.revokeTokenAndLogout();
    }

    isLoggedIn(): boolean {
        return this.oAuthService.hasValidIdToken();
    }
}
