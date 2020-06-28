import {Component, OnInit} from '@angular/core';
import {AccountData, UpdateAccountDataDto} from '../../services/dtos/Dtos';
import {AccountService} from '../../services/account/account.service';
import {InfoService} from '../../services/info/info.service';

@Component({
    selector: 'app-account-settings-page',
    templateUrl: './account-settings-page.component.html',
    styles: ['']
})
export class AccountSettingsPageComponent implements OnInit {

    accountData: AccountData | undefined;


    constructor(
        private accountService: AccountService,
        private infoService: InfoService
    ) {
    }

    ngOnInit(): void {
        this.accountService.getAccountData().subscribe(
            data => this.accountData = data,
            error => this.infoService.showErrorMessage('Ein unerwarteter Fehler ist aufgetreten')
        );
    }

    onSaveProfileSettings(updateAccountDataDto: UpdateAccountDataDto) {
        this.accountService.updateAccountData(updateAccountDataDto)
            .subscribe(() => this.infoService.showMessage('Die Daten wurden aktualisiert'));
    }
}
