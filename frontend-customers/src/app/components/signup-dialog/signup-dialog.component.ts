import {Component, Inject} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {AccountData} from '../../services/dtos/Dtos';

@Component({
    selector: 'app-signup-dialog',
    templateUrl: './signup-dialog.component.html',
    styleUrls: ['./signup-dialog.component.scss']
})
export class SignupDialogComponent {

    profileForm: FormGroup = this.formBuilder.group({
        profile: []
    });

    constructor(private formBuilder: FormBuilder, private dialog: MatDialogRef<SignupDialogComponent>,
                @Inject(MAT_DIALOG_DATA) public data: AccountData) {
    }

    onSubmit() {
        if (this.profileForm.valid) {
            this.dialog.close(this.profileForm.controls.profile.value);
        }
    }

}
