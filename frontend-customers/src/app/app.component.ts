import {Component, OnInit} from '@angular/core';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {SignupDialogComponent} from './components/signup-dialog/signup-dialog.component';
import {AccountService} from './services/account/account.service';
import {AuthService} from './auth/auth.service';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

    accountDataVerified = false;

    constructor(private dialog: MatDialog, private accountService: AccountService, private authService: AuthService) {
    }

    ngOnInit(): void {
        if (this.authService.isLoggedIn() && !this.accountDataVerified) {
            this.accountService.getAccountData().subscribe(data => {
                data.name = this.authService.getGivenName();
                data.lastname = this.authService.getFamilyName();

                if (Object.values(data).some(value => value === null)) {
                    const dialogConfig: MatDialogConfig = {
                        disableClose: true,
                        data
                    };

                    this.dialog.open(SignupDialogComponent, dialogConfig).afterClosed().subscribe(accountData => {
                        this.accountDataVerified = true;
                        this.accountService.finishRegistration(accountData).subscribe();
                    });
                }
            });
        }
    }
}
