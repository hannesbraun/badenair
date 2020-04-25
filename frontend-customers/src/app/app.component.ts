import {AfterContentChecked, Component, OnDestroy, OnInit} from '@angular/core';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {SignupDialogComponent} from './components/signup-dialog/signup-dialog.component';
import {AccountService} from './services/account/account.service';
import {AuthService} from './auth/auth.service';
import {Subject} from 'rxjs';
import {filter, first} from 'rxjs/operators';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, AfterContentChecked, OnDestroy {

    accountDataVerified = false;
    isLoggedInSubject = new Subject<boolean>();

    constructor(private dialog: MatDialog, private accountService: AccountService, private authService: AuthService) {
    }

    ngOnInit(): void {
        this.isLoggedInSubject.asObservable()
            .pipe(
                filter(Boolean),
                first()
            )
            .subscribe(() => {
                if (!this.accountDataVerified) {
                    this.accountService.getAccountData().subscribe(data => {
                        if (Object.values(data).some(value => value === null)) {
                            const dialogConfig: MatDialogConfig = {
                                disableClose: true
                            };

                            this.dialog.open(SignupDialogComponent, dialogConfig).afterClosed().subscribe(accountData => {
                                this.accountDataVerified = true;
                                delete accountData.name;
                                delete accountData.lastname;
                                this.accountService.finishRegistration(accountData).subscribe();
                            });
                        }
                    });
                }
            });
    }

    ngOnDestroy(): void {
        this.isLoggedInSubject.unsubscribe();
    }

    ngAfterContentChecked(): void {
        this.isLoggedInSubject.next(this.authService.isLoggedIn());
    }
}
