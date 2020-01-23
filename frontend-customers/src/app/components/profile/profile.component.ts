import {Component, EventEmitter, Output} from '@angular/core';
import {SignUpDto, UpdateProfileDto} from '../../services/dtos/Dtos';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
    selector: 'app-profile',
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.scss']
})
export class ProfileComponent {

    @Output() saveProfileSettings: EventEmitter<UpdateProfileDto> = new EventEmitter();

    profileForm: FormGroup = this.formBuilder.group({
        profile: [],
        email: ['', [Validators.required, Validators.email]]
    });

    constructor(private formBuilder: FormBuilder) {
    }

    onSubmit() {
        if (this.profileForm.valid) {
            this.saveProfileSettings.emit({
                ...this.profileForm.controls.profile.value,
                email: this.getFormControlValue('email'),
            } as SignUpDto);
        }
    }

    private getFormControlValue(formControlName: string): string | null {
        const formControl = this.profileForm.get(formControlName);
        return formControl ? formControl.value : null;
    }


}
