import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AuthConfig, OAuthService} from 'angular-oauth2-oidc';
import {environment} from '../../environments/environment';


@NgModule({
    declarations: [],
    imports: [
        CommonModule
    ]
})
export class AuthModule {

    private static readonly authConfig: AuthConfig = {
        // Url of the Identity Provider
        issuer: environment.authUrl,
        redirectUri: window.location.origin,
        clientId: 'badenair-employee',
        responseType: 'code',
        scope: 'openid profile email offline_access employees',
        // TODO: Enable HTTPS
        requireHttps: false
    };

    constructor(private oauthService: OAuthService) {
        this.configure();
    }

    private configure() {
        this.oauthService.configure(AuthModule.authConfig);
        this.oauthService.loadDiscoveryDocumentAndTryLogin();
    }
}
