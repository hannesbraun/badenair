import {Component, forwardRef, OnDestroy} from '@angular/core';
import {
    ControlValueAccessor,
    FormBuilder,
    FormControl,
    FormGroup,
    NG_VALIDATORS,
    NG_VALUE_ACCESSOR,
    Validators
} from '@angular/forms';
import {Subscription} from 'rxjs';

export interface ProfileFormValue {
    lastname: string;
    name: string;
    birthDate: Date;
    street: string;
    zipCode: string;
    placeOfResidence: string;
    cardOwner: string;
    cardNumber: string;
    check: string;
    invalidationDate: Date;
}

@Component({
    selector: 'app-profile-form',
    templateUrl: './profile-form.component.html',
    styleUrls: ['./profile-form.component.scss'],
    providers: [
        {
            provide: NG_VALUE_ACCESSOR,
            useExisting: forwardRef(() => ProfileFormComponent),
            multi: true
        },
        {
            provide: NG_VALIDATORS,
            useExisting: forwardRef(() => ProfileFormComponent),
            multi: true
        }
    ]
})
export class ProfileFormComponent implements ControlValueAccessor, OnDestroy {

    private static readonly DAY_IN_MILLISECONDS = 24 * 60 * 60 * 1000;

    profileForm: FormGroup = this.formBuilder.group({
        lastname: ['', Validators.required],
        name: ['', Validators.required],
        birthDate: ['', Validators.required],
        street: ['', Validators.required],
        zipCode: ['', [Validators.required, Validators.pattern('[0-9]{5}')]],
        placeOfResidence: ['', Validators.required],
        cardOwner: ['', Validators.required],
        cardNumber: ['', Validators.required],
        check: ['', Validators.required],
        invalidationDate: ['', Validators.required]
    });

    maxDateBirthDate: Date | undefined;
    minDateBirthDate: Date | undefined;
    minDateInvalidationDate: Date | undefined;
    subscriptions: Subscription[] = [];
    onChange: any = () => {};
    onTouched: any = () => {};

    constructor(private formBuilder: FormBuilder) {
        this.initDateBoundaries();

        this.subscriptions.push(
            this.profileForm.valueChanges.subscribe(value => {
                this.onChange(value);
                this.onTouched();
            })
        );
    }

    ngOnDestroy(): void {
        this.subscriptions.forEach(s => s.unsubscribe());
    }

    registerOnChange(fn: any): void {
        this.onChange = fn;
    }

    registerOnTouched(fn: any): void {
        this.onTouched = fn;
    }

    writeValue(value: ProfileFormValue): void {
        if (value) {
            this.value = value;
        }

        if (value === null) {
            this.profileForm.reset();
        }
    }

    validate(_: FormControl) {
        return this.profileForm.valid ? null : {profileForm: {valid: false}};
    }

    get value(): ProfileFormValue {
        return this.profileForm.value;
    }

    set value(value: ProfileFormValue) {
        this.profileForm.setValue(value);
        this.onChange(value);
        this.onTouched();
    }

    private initDateBoundaries() {
        const now = new Date();
        const yearOfMinimumAge = now.getFullYear() - 18;

        this.minDateBirthDate = new Date(1900, 0, 1);
        this.maxDateBirthDate = new Date(now);
        this.maxDateBirthDate.setFullYear(yearOfMinimumAge);
        this.minDateInvalidationDate = new Date(now);
        this.minDateInvalidationDate.setTime(now.getTime() + ProfileFormComponent.DAY_IN_MILLISECONDS);
    }

}
