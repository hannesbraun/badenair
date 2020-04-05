import {Component} from '@angular/core';
import {BreakpointObserver, Breakpoints} from '@angular/cdk/layout';
import {Observable} from 'rxjs';
import {map, shareReplay} from 'rxjs/operators';
import {AuthService} from '../../auth/auth.service';

@Component({
    selector: 'app-navigation',
    templateUrl: './navigation.component.html',
    styleUrls: ['./navigation.component.scss']
})
export class NavigationComponent {

    isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.XSmall)
        .pipe(
            map(result => result.matches),
            shareReplay()
        );

    constructor(private breakpointObserver: BreakpointObserver, private authService: AuthService) {
    }

    login() {
        this.authService.login();
    }

    logout() {
        this.authService.logout();
    }

    get isLoggedIn() {
        return this.authService.isLoggedIn();
    }

    isPilot = () => this.authService.isPilot();

    isTechnician = () => this.authService.isTechnician();

    isGround = () => this.authService.isGround();

    isFlightDirector = () => this.authService.isFlightDirector();
}
