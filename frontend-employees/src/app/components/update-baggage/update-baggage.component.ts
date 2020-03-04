import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {BaggageState, ChangeBaggageStateDto, FlightDto} from '../../services/dtos/Dtos';

@Component({
    selector: 'app-update-baggage',
    templateUrl: './update-baggage.component.html',
    styleUrls: ['./update-baggage.component.scss']
})
export class UpdateBaggageComponent {

    @Input() availableFlights !: FlightDto[];
    @Output() updateBaggageState = new EventEmitter<ChangeBaggageStateDto>();

    updateBaggageForm: FormGroup = this.formBuilder.group({
        flightSelect: ['', Validators.required],
        stateSelect: ['', Validators.required],
        baggageId: ['', Validators.required]
    });

    constructor(private formBuilder: FormBuilder) {
    }

    onSubmit() {
        if (this.updateBaggageForm.valid) {
            const dto: ChangeBaggageStateDto = {
                flightId: this.updateBaggageForm.get('flightSelect')?.value,
                baggageId: this.updateBaggageForm.get('baggageId')?.value,
                state: this.updateBaggageForm.get('stateSelect')?.value
            };
            this.updateBaggageState.emit(dto);
        }
    }

    get baggageStates() {
        return BaggageState;
    }

}
