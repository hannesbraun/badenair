import {Component, EventEmitter, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {passwordsMatchValidator} from '../../services/util/FormValidators';
import {SignUpDto} from '../../services/dtos/Dtos';

@Component({
    selector: 'app-sign-up',
    templateUrl: './sign-up.component.html',
    styleUrls: ['./sign-up.component.scss']
})
export class SignUpComponent {

    @Output() signUpSubmit: EventEmitter<SignUpDto> = new EventEmitter();

    signUpForm: FormGroup = this.formBuilder.group({
        profile: [],
        email: ['', [Validators.required, Validators.email]],
        password: ['', Validators.required],
        passwordConfirm: ['', Validators.required]
    }, {validators: passwordsMatchValidator});

    constructor(private formBuilder: FormBuilder) {
    }

    onSubmit() {
        if (this.signUpForm.valid) {
            this.signUpSubmit.emit({
                ...this.signUpForm.controls.profile.value,
                email: this.getFormControlValue('email'),
                password: this.getFormControlValue('password')
            } as SignUpDto);
        }
    }

    private getFormControlValue(formControlName: string): string | Date | null {
        const formControl = this.signUpForm.get(formControlName);
        return formControl ? formControl.value : null;
    }
}
