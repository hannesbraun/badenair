import {Component} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MatDialogRef} from '@angular/material';
import {passwordsMatchValidator} from '../../../services/util/FormValidators';

export interface ChangePasswordDialogOutput {
    oldPassword: string;
    newPassword: string;
}

@Component({
    selector: 'app-change-password-dialog',
    templateUrl: './change-password-dialog.component.html',
    styleUrls: ['./change-password-dialog.component.scss']
})
export class ChangePasswordDialogComponent {

    changePasswordForm: FormGroup;

    constructor(private dialog: MatDialogRef<ChangePasswordDialogComponent>, private fb: FormBuilder) {
        this.changePasswordForm = this.fb.group({
            oldPassword: ['', Validators.required],
            password: ['', Validators.required],
            passwordConfirm: ['', Validators.required]
        }, {validators: passwordsMatchValidator});
    }

    onClickClose(event: Event) {
        event.preventDefault();
        this.dialog.close();
    }

    onSubmit() {
        const oldPassword = this.changePasswordForm.get('oldPassword');
        const newPassword = this.changePasswordForm.get('password');

        if (this.changePasswordForm.valid && oldPassword && newPassword) {
            const output: ChangePasswordDialogOutput = {
                oldPassword: oldPassword.value,
                newPassword: newPassword.value
            };
            this.dialog.close(output);
        }
    }
}
