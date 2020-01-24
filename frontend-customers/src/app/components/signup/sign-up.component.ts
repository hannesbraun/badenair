import {Component, EventEmitter, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {passwordsMatchValidator} from '../../services/util/FormValidators';
import {SignUpDto} from '../../services/dtos/Dtos';
import {getFormControlValueFunction} from '../../services/util/FormUtils';

@Component({
    selector: 'app-sign-up',
    templateUrl: './sign-up.component.html',
    styleUrls: ['./sign-up.component.scss']
})
export class SignUpComponent {

    private readonly getFormControlValue: (formControlName: string) => any | null;

    @Output() signUpSubmit: EventEmitter<SignUpDto> = new EventEmitter();

    signUpForm: FormGroup = this.formBuilder.group({
        profile: [],
        email: ['', [Validators.required, Validators.email]],
        password: ['', Validators.required],
        passwordConfirm: ['', Validators.required]
    }, {validators: passwordsMatchValidator});

    constructor(private formBuilder: FormBuilder) {
        this.getFormControlValue = getFormControlValueFunction(this.signUpForm);
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
}
