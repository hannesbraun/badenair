import {Component, forwardRef, Input, OnDestroy, OnInit} from '@angular/core';
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
import {AccountData} from '../../services/dtos/Dtos';

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
export class ProfileFormComponent implements ControlValueAccessor, OnInit, OnDestroy {

    private static readonly DAY_IN_MILLISECONDS = 24 * 60 * 60 * 1000;

    @Input() initialData ?: AccountData;

    profileForm: FormGroup | undefined;

    maxDateBirthDate: Date | undefined;
    minDateBirthDate: Date | undefined;
    minDateInvalidationDate: Date | undefined;
    subscriptions: Subscription[] = [];
    onChange: any = () => {};
    onTouched: any = () => {};

    constructor(private formBuilder: FormBuilder) {
        this.initDateBoundaries();
    }

    ngOnInit(): void {
        this.profileForm = this.formBuilder.group({
            birthDate: [this.initialValue('birthDate'), Validators.required],
            street: [this.initialValue('street'), Validators.required],
            zipCode: [this.initialValue('zipCode'), [Validators.required, Validators.pattern('[0-9]{5}')]],
            placeOfResidence: [this.initialValue('placeOfResidence'), Validators.required],
            cardOwner: [this.initialValue('cardOwner'), Validators.required],
            cardNumber: [this.initialValue('cardNumber'), Validators.required],
            check: [this.initialValue('check'), Validators.required],
            invalidationDate: [this.initialValue('invalidationDate'), Validators.required]
        });

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
    }

    validate(_: FormControl) {
        return this.profileForm?.valid ? null : {profileForm: {valid: false}};
    }

    get value(): ProfileFormValue {
        return this.profileForm?.value;
    }

    set value(value: ProfileFormValue) {
        this.profileForm?.setValue(value);
        this.onChange(value);
        this.onTouched();
    }

    private initialValue(key: string): string | Date {
        if (!this.initialData) {
            return '';
        }

        return this.initialData[key] ?? '';
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
