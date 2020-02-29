import {Component, EventEmitter, Output} from '@angular/core';
import {SignUpDto, UpdateProfileDto} from '../../services/dtos/Dtos';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MatDialog} from '@angular/material/dialog';
import {
    ChangePasswordDialogComponent,
    ChangePasswordDialogOutput
} from './change-password-dialog/change-password-dialog.component';

@Component({
    selector: 'app-profile',
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.scss']
})
export class ProfileComponent {

    @Output() saveProfileSettings: EventEmitter<UpdateProfileDto> = new EventEmitter();
    @Output() changePassword: EventEmitter<ChangePasswordDialogOutput> = new EventEmitter();

    profileForm: FormGroup = this.formBuilder.group({
        profile: [],
        email: ['', [Validators.required, Validators.email]]
    });

    constructor(private formBuilder: FormBuilder, private dialog: MatDialog) {
    }

    onSubmit() {
        if (this.profileForm.valid) {
            this.saveProfileSettings.emit({
                ...this.profileForm.controls.profile.value,
                email: this.profileForm.get('email')?.value,
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
