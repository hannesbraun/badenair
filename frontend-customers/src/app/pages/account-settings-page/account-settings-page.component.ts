import {Component, OnInit} from '@angular/core';
import {AccountData, UpdateProfileDto} from '../../services/dtos/Dtos';
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

    onSaveProfileSettings(profileDto: UpdateProfileDto) {
        // TODO: Call service to handle updating profile
        console.log(profileDto);
    }
}
