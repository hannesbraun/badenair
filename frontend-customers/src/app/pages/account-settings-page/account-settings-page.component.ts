import {Component} from '@angular/core';
import {UpdateProfileDto} from '../../services/dtos/Dtos';
import {ChangePasswordDialogOutput} from '../../components/profile/change-password-dialog/change-password-dialog.component';

@Component({
    selector: 'app-account-settings-page',
    templateUrl: './account-settings-page.component.html',
    styles: ['']
})
export class AccountSettingsPageComponent {

    onSaveProfileSettings(profileDto: UpdateProfileDto) {
        // TODO: Call service to handle updating profile
        console.log(profileDto);
    }

    onChangePassword(changePasswordDialogOutput: ChangePasswordDialogOutput) {
        // TODO: Call service to handle password change
        console.log(changePasswordDialogOutput);
    }
}
