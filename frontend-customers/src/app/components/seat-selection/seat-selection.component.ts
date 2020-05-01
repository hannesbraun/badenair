import {Component, EventEmitter, Output} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';

@Component({
    selector: 'app-seat-selection',
    templateUrl: './seat-selection.component.html',
    styleUrls: ['./seat-selection.component.scss']
})
export class SeatSelectionComponent {
    columns = ['A', 'B', 'C', 'D', 'E', 'F'];
    rows: number[] = [];

    seatForm = this.formBuilder.group({
        row: ['', Validators.required],
        column: ['', Validators.required],
    });

    @Output() seatSelected = new EventEmitter<any>();

    constructor(private formBuilder: FormBuilder) {
        for (let i = 0; i < 29; i++) {
            this.rows.push(i + 1);
        }
    }

    submit() {
        if (this.seatForm.invalid) {
            return;
        }
        this.seatSelected.emit(this.seatForm.value);
    }
}
