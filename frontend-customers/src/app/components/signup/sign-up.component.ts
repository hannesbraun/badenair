import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {passwordsMatchValidator} from '../../services/util/FormValidators';
import {SignUpDto} from '../../services/dtos/Dtos';

@Component({
    selector: 'app-sign-up',
    templateUrl: './sign-up.component.html',
    styleUrls: ['./sign-up.component.scss']
})
export class SignUpComponent implements OnInit {

    @Output() signUpSubmit: EventEmitter<SignUpDto> = new EventEmitter();

    signUpForm: FormGroup = this.fb.group({
        lastname: ['', Validators.required],
        name: ['', Validators.required],
        birthDate: ['', Validators.required],
        street: ['', Validators.required],
        zipCode: ['', [Validators.required, Validators.pattern('[0-9]{5}')]],
        placeOfResidence: ['', Validators.required],
        cardOwner: ['', Validators.required],
        cardNumber: ['', Validators.required],
        check: ['', Validators.required],
        invalidationDate: ['', Validators.required],
        email: ['', [Validators.required, Validators.email]],
        password: ['', Validators.required],
        passwordConfirm: ['', Validators.required]
    }, {validators: passwordsMatchValidator});

    maxDateBirthDate: Date | undefined;
    minDateBirthDate: Date | undefined;
    minDateInvalidationDate: Date | undefined;

    constructor(private fb: FormBuilder) {
    }

    ngOnInit() {
        this.initDateBoundaries();
    }

    onSubmit() {
        if (this.signUpForm.valid) {
            this.signUpSubmit.emit({
                lastname: this.getFormControlValue('lastname'),
                name: this.getFormControlValue('name'),
                birthDate: this.getFormControlValue('birthDate'),
                street: this.getFormControlValue('street'),
                zipCode: this.getFormControlValue('zipCode'),
                placeOfResidence: this.getFormControlValue('placeOfResidence'),
                cardOwner: this.getFormControlValue('cardOwner'),
                cardNumber: this.getFormControlValue('cardNumber'),
                check: this.getFormControlValue('check'),
                invalidationDate: this.getFormControlValue('invalidationDate'),
                email: this.getFormControlValue('email'),
                password: this.getFormControlValue('password')
            } as SignUpDto);
        }
    }

    private getFormControlValue(formControlName: string): string | Date | null {
        const formControl = this.signUpForm.get(formControlName);
        return formControl ? formControl.value : null;
    }

    private initDateBoundaries() {
        this.minDateBirthDate = new Date(1900, 0, 1);
        this.maxDateBirthDate = new Date();
        this.maxDateBirthDate.setFullYear(new Date().getFullYear() - 18);
        this.minDateInvalidationDate = new Date();
        this.minDateInvalidationDate.setTime(new Date().getTime() + 24 * 60 * 60 * 1000);
    }
}
