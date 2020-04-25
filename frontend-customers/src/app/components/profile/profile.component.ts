import {Component, EventEmitter, Input, Output} from '@angular/core';
import {AccountData, SignUpDto, UpdateProfileDto} from '../../services/dtos/Dtos';
import {FormBuilder, FormGroup} from '@angular/forms';
import {environment} from '../../../environments/environment';

@Component({
    selector: 'app-profile',
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.scss']
})
export class ProfileComponent {

    @Input() initialData ?: AccountData;
    @Output() saveProfileSettings: EventEmitter<UpdateProfileDto> = new EventEmitter();

    profileForm: FormGroup = this.formBuilder.group({
        profile: []
    });

    constructor(private formBuilder: FormBuilder) {
    }

    onSubmit() {
        if (this.profileForm.valid) {
            this.saveProfileSettings.emit({
                ...this.profileForm.controls.profile.value,
                email: this.profileForm.get('email')?.value,
            } as SignUpDto);
        }
    }

    get accountUrl(): string {
        return `${environment.authUrl}/account`;
    }
}
