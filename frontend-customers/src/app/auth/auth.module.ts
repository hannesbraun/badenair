import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AuthConfig, OAuthService} from 'angular-oauth2-oidc';
import {environment} from '../../environments/environment';
import {Router} from '@angular/router';


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
        clientId: 'badenair-customer',
        responseType: 'code',
        scope: 'openid profile email offline_access customers',
        postLogoutRedirectUri: environment.home,
        // TODO: Enable HTTPS
        requireHttps: false
    };

    constructor(private oauthService: OAuthService, private router: Router) {
        this.configure();
    }

    private configure() {
        this.oauthService.configure(AuthModule.authConfig);
        this.oauthService.loadDiscoveryDocumentAndTryLogin();
        this.oauthService.events.subscribe(event => {
            if (event.type === 'token_received') {
                const url = decodeURIComponent(this.oauthService.state ?? '');
                this.router.navigate([url]);
            }
        });
    }
}
