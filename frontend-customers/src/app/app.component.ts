import {Component, OnInit} from '@angular/core';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {SignupDialogComponent} from './components/signup-dialog/signup-dialog.component';
import {AccountService} from './services/account/account.service';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

    accountDataVerified = false;

    constructor(private dialog: MatDialog, private accountService: AccountService) {
    }

    ngOnInit(): void {
        if (!this.accountDataVerified) {
            this.accountService.getAccountData().subscribe(data => {
                if (Object.values(data).some(value => value === undefined)) {
                    const dialogConfig: MatDialogConfig = {
                        disableClose: true
                    };

                    this.dialog.open(SignupDialogComponent, dialogConfig).afterClosed().subscribe(value => this.accountDataVerified = true);
                }
            });
        }
    }
}
