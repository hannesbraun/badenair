import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {AbstractControl, FormArray, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {PassengerDto} from 'src/app/services/dtos/Dtos';
import {Observable} from 'rxjs';

@Component({
    selector: 'app-passenger-form',
    templateUrl: './passengers-form.component.html',
    styleUrls: ['./passengers-form.component.scss']
})
export class PassengersFormComponent implements OnInit {
    baggageCapacity = [15, 23, 30];
    baggagePrice = 2;
    form!: FormGroup;

    @Input() passengers!: Observable<PassengerDto[]>;
    @Input() passengerCount!: Observable<number>;
    @Output() passengersSubmit = new EventEmitter<PassengerDto[]>();

    constructor(private formBuilder: FormBuilder,
    ) {
    }

    ngOnInit() {
        this.passengerCount.subscribe(value => {
            this.form = this.formBuilder.group({
                items: this.formBuilder.array([])
            });
            this.createFormArray(value);
        });
        this.passengers.subscribe(value => {
            if (!value) {
                return;
            }
            this.form.controls.items.patchValue(value);
        });
    }

    createFormArray(count: number) {
        for (let i = 0; i < count; i++) {
            this.appendPassenger();
        }
    }

    submit() {
        if (this.form.invalid) {
            return;
        }
        const formModel = this.form.controls.items.value;
        const passengers: PassengerDto[] = formModel.map(
            (passenger: PassengerDto) => Object.assign({}, passenger)
        );

        this.passengersSubmit.emit(passengers);
    }

    get formArray(): FormArray {
        return (this.form.controls.items as FormArray);
    }

    get forms(): AbstractControl[] {
        return this.formArray.controls;
    }

    appendPassenger() {
        this.formArray.push(this.formBuilder.group({
            name: ['', Validators.required],
            surname: ['', Validators.required],
            baggage1: [0],
            baggage2: [0],
            baggage3: [0],
            baggage4: [0]
        }));
    }
}
