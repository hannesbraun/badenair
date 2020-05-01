import {Component, EventEmitter, Output} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';

@Component({
    selector: 'app-seat-selection',
    templateUrl: './seat-selection.component.html',
    styleUrls: ['./seat-selection.component.scss']
})
export class SeatSelectionComponent {
    rows = ['A', 'B'];
    columns = [3, 4, 7, 5];

    seatForm = this.formBuilder.group({
        row: ['', Validators.required],
        column: ['', Validators.required],
    });

    @Output() seatSelected = new EventEmitter<any>();

    constructor(private formBuilder: FormBuilder) {
    }

    submit() {
        if (this.seatForm.invalid) {
            return;
        }
        this.seatSelected.emit(this.seatForm.value);
    }
}
