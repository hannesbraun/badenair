import {Injectable} from '@angular/core';
import {OAuthService} from 'angular-oauth2-oidc';

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    private readonly PILOT_ROLE = 'ROLE_PILOT';
    private readonly FLIGHT_DIRECTOR_ROLE = 'ROLE_FLIGHT_DIRECTOR';
    private readonly TECHNICIAN_ROLE = 'ROLE_TECHNICIAN';
    private readonly GROUND_ROLE = 'ROLE_GROUND';

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

    isPilot(): boolean {
        return this.userRoles.includes(this.PILOT_ROLE);
    }

    isFlightDirector(): boolean {
        return this.userRoles.includes(this.FLIGHT_DIRECTOR_ROLE);
    }

    isTechnician(): boolean {
        return this.userRoles.includes(this.TECHNICIAN_ROLE);
    }

    isGround(): boolean {
        return this.userRoles.includes(this.GROUND_ROLE);
    }

    private get userRoles(): string[] {
        const roles = this.oAuthService.getIdentityClaims() as { roles: string[] };
        return roles?.roles ?? [];
    }
}
