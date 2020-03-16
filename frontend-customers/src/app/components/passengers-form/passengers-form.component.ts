import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {AbstractControl, FormArray, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {PassengerDto} from 'src/app/services/dtos/Dtos';
import { Router } from '@angular/router';

@Component({
    selector: 'app-passenger-form',
    templateUrl: './passengers-form.component.html',
    styleUrls: ['./passengers-form.component.scss']
})
export class PassengersFormComponent implements OnInit {

    form!: FormGroup;
    passengerCount!: number;
    baggageCapacity = [15, 23, 30];

    @Output() onPassengersSubmit = new EventEmitter<PassengerDto[]>();

    constructor(private formBuilder: FormBuilder, private router: Router) {
    }

    ngOnInit() {
        this.passengerCount = 1; // TODO: Get passengers count from flightsearch
        this.form = this.formBuilder.group({
            items: this.formBuilder.array([])
        });
        this.createFormArray();
    }

    createFormArray() {
        for (let i = 0; i < this.passengerCount; i++) {
            this.appendPassenger();
        }
    }

    submit() {
        const formModel = this.form.controls.items.value;

        const passengers: PassengerDto[] = formModel.map(
            (passenger: PassengerDto) => Object.assign({}, passenger)
        );

        this.onPassengersSubmit.emit(passengers);
        
        if (this.form.controls.items.valid) {
            this.router.navigate(['overview']);
        }
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

    removePassenger(index: number) {
        this.formArray.removeAt(index);
    }
}
