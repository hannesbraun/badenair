import {Component, OnInit} from '@angular/core';
import {AccountData, UpdateAccountDataDto} from '../../services/dtos/Dtos';
import {AccountService} from '../../services/account/account.service';

@Component({
    selector: 'app-account-settings-page',
    templateUrl: './account-settings-page.component.html',
    styles: ['']
})
export class AccountSettingsPageComponent implements OnInit {

    accountData: AccountData | undefined;


    constructor(private accountService: AccountService) {
    }

    ngOnInit(): void {
        this.accountService.getAccountData().subscribe(data => {
            this.accountData = data;
        });
    }

    onSaveProfileSettings(updateAccountDataDto: UpdateAccountDataDto) {
        this.accountService.updateAccountData(updateAccountDataDto).subscribe();
    }
}
