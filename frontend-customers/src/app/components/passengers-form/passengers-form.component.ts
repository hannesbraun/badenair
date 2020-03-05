import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {AbstractControl, FormArray, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {PassengerDto} from 'src/app/services/dtos/Dtos';

@Component({
    selector: 'app-passenger-form',
    templateUrl: './passengers-form.component.html',
    styleUrls: ['./passengers-form.component.scss']
})
export class PassengersFormComponent implements OnInit {

    form!: FormGroup;
    passengerCount!: number;
    baggageCapacity = [15, 23, 30];
    selectedBaggage = 0;

    @Output() onPassengersSubmit = new EventEmitter<PassengerDto[]>();


    constructor(private formBuilder: FormBuilder) {
    }

    ngOnInit() {
        this.passengerCount = 3; // TODO: Get passengers count from flightsearch
        this.form = this.formBuilder.group({
            items: this.formBuilder.array([])
        });
        this.createFormArray();
    }

    createFormArray() {
        for (let i = 0; i < this.passengerCount; i++) {
            (this.form.controls.items as FormArray)
                .push(this.formBuilder.group({
                    name: ['', Validators.required],
                    surname: ['', Validators.required],
                    baggage1: [0],
                    baggage2: [0],
                    baggage3: [0],
                    baggage4: [0]
                }));
        }
    }

    submit() {
        const formModel = this.form.controls.items.value;

        const passengers: PassengerDto[] = formModel.map(
            (passenger: PassengerDto) => Object.assign({}, passenger)
        );

        this.onPassengersSubmit.emit(passengers);
    }

    get forms(): AbstractControl[] {
        return (this.form.controls.items as FormArray).controls;
    }
}
