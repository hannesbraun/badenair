import {Component} from '@angular/core';
import {BreakpointObserver, Breakpoints} from '@angular/cdk/layout';
import {Observable} from 'rxjs';
import {map, shareReplay} from 'rxjs/operators';
import {Router} from '@angular/router';

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
    loggedIn = true;

    constructor(private breakpointObserver: BreakpointObserver, private router: Router) {
    }

    login() {
        if (!this.loggedIn) {
            this.router.navigate(['/']);
        }
        this.loggedIn = !this.loggedIn;
    }

    openMyAccount() {

    }

}
