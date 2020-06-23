import {Component, EventEmitter, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {BaggageState, ChangeBaggageStateDto} from '../../services/dtos/Dtos';

@Component({
    selector: 'app-update-baggage',
    templateUrl: './update-baggage.component.html',
    styleUrls: ['./update-baggage.component.scss']
})
export class UpdateBaggageComponent {

    @Output() updateBaggageState = new EventEmitter<ChangeBaggageStateDto>();

    updateBaggageForm: FormGroup = this.formBuilder.group({
        stateSelect: ['', Validators.required],
        baggageId: ['', Validators.required]
    });

    constructor(private formBuilder: FormBuilder) {
    }

    onSubmit() {
        if (this.updateBaggageForm.valid) {
            const dto: ChangeBaggageStateDto = {
                id: this.updateBaggageForm.get('baggageId')?.value,
                state: this.updateBaggageForm.get('stateSelect')?.value
            };
            this.updateBaggageState.emit(dto);
        }
    }

    get baggageStates() {
        return BaggageState;
    }

}
