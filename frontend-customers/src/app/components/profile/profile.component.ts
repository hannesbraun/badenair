import {Component, EventEmitter, Output} from '@angular/core';
import {SignUpDto, UpdateProfileDto} from '../../services/dtos/Dtos';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MatDialog} from '@angular/material';
import {
    ChangePasswordDialogComponent,
    ChangePasswordDialogOutput
} from './change-password-dialog/change-password-dialog.component';
import {getFormControlValueFunction} from '../../services/util/FormUtils';

@Component({
    selector: 'app-profile',
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.scss']
})
export class ProfileComponent {

    private readonly getFormControlValue: (formControlName: string) => any | null;

    @Output() saveProfileSettings: EventEmitter<UpdateProfileDto> = new EventEmitter();
    @Output() changePassword: EventEmitter<ChangePasswordDialogOutput> = new EventEmitter();

    profileForm: FormGroup = this.formBuilder.group({
        profile: [],
        email: ['', [Validators.required, Validators.email]]
    });

    constructor(private formBuilder: FormBuilder, private dialog: MatDialog) {
        this.getFormControlValue = getFormControlValueFunction(this.profileForm);
    }

    onSubmit() {
        if (this.profileForm.valid) {
            this.saveProfileSettings.emit({
                ...this.profileForm.controls.profile.value,
                email: this.getFormControlValue('email'),
            } as SignUpDto);
        }
    }

    onClickChangePassword() {
        this.dialog.open(ChangePasswordDialogComponent).afterClosed().subscribe((output: ChangePasswordDialogOutput) => {
            if (output) {
                this.changePassword.emit(output);
            }
        });
    }
}
