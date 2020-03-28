import {Component} from '@angular/core';
import {BreakpointObserver, Breakpoints} from '@angular/cdk/layout';
import {Observable} from 'rxjs';
import {map, shareReplay} from 'rxjs/operators';
import {Router} from '@angular/router';
import {MatDialog} from '@angular/material/dialog';
import {LoginComponent} from '../login/login.component';
import {User, UserType} from '../../services/dtos/Dtos';

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
    userType !: UserType;

    constructor(private breakpointObserver: BreakpointObserver,
                private router: Router,
                private dialog: MatDialog) {
    }

    login() {
        if (!this.loggedIn) {
            const loginDialog = this.dialog.open(LoginComponent);
            loginDialog.afterClosed()
                .subscribe((user: User) => {
                    if (user) {
                        this.userType = user.type;
                        this.loggedIn = true;
                    }
                });
        } else {
            this.loggedIn = false;
            this.router.navigate(['/']);
        }
    }

    isPilot = () => this.userType === UserType.pilot;

    isTechnician = () => this.userType === UserType.technician;

    isGround = () => this.userType === UserType.ground;

    isFlightDirector = () => this.userType === UserType.flightDirector;
}
