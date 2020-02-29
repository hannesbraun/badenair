import {Component} from '@angular/core';
import {BreakpointObserver, Breakpoints} from '@angular/cdk/layout';
import {Observable} from 'rxjs';
import {map, shareReplay} from 'rxjs/operators';
import {MatDialog} from '@angular/material/dialog';
import {LoginComponent} from '../login/login.component';
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
    loggedIn = false;


    constructor(private breakpointObserver: BreakpointObserver,
                private dialog: MatDialog,
                private router: Router
    ) {
    }

    login() {
        if (!this.loggedIn) {
            const loginDialog = this.dialog.open(LoginComponent);
            loginDialog.afterClosed()
                .subscribe((output: boolean) => {
                    if (output) {
                        this.loggedIn = output;
                    }
                });
        } else {
            this.loggedIn = false;
            this.router.navigate(['/']);
        }
    }
}
