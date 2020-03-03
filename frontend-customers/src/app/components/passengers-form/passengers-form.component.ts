import { Component, OnInit, Output,EventEmitter } from '@angular/core';
import { FormGroup, FormBuilder, FormArray, Validators } from '@angular/forms';
import { PassengerDto } from 'src/app/services/dtos/Dtos';

@Component({
  selector: 'app-passenger-form',
  templateUrl: './passenger-form.component.html',
  styleUrls: ['./passenger-form.component.scss']
})
export class PassengerFormComponent implements OnInit {

  form!: FormGroup;
  passengerCount!: number;
  baggageCapacity = [15, 23, 30];
  selectedBaggage = 0;

  @Output() onPassengersSubmit = new EventEmitter<PassengerDto[]>();


  constructor(private fb: FormBuilder) { }

  ngOnInit() {
    this.passengerCount = 3; //TODO: Get passengers count from flightsearch
    this.form = this.fb.group({
      items: this.fb.array([])
    });
    this.createFormArray();
  }

  createFormArray() {
    for (let i = 0; i < this.passengerCount; i++) {
      (this.form.controls['items'] as FormArray).push(this.fb.group({
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
    const formModel = this.form.controls['items'].value;

    const passengers : PassengerDto[] = formModel.map(
      (passenger: PassengerDto) => Object.assign({}, passenger)
    );

    this.onPassengersSubmit.emit(passengers);
  }
}
