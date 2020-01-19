import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {passwordsMatchValidator} from '../../services/util/FormValidators';

@Component({
    selector: 'app-sign-up',
    templateUrl: './sign-up.component.html',
    styleUrls: ['./sign-up.component.scss']
})
export class SignUpComponent implements OnInit {

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
        console.log('onSubmit');
    }

    private initDateBoundaries() {
        this.minDateBirthDate = new Date(1900, 0, 1);
        this.maxDateBirthDate = new Date();
        this.maxDateBirthDate.setFullYear(new Date().getFullYear() - 18);
        this.minDateInvalidationDate = new Date();
        this.minDateInvalidationDate.setTime(new Date().getTime() + 24 * 60 * 60 * 1000);
    }
}
