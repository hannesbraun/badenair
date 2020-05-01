import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {AbstractControl, FormArray, FormBuilder, Validators} from '@angular/forms';

@Component({
    selector: 'app-seat-selection',
    templateUrl: './seat-selection.component.html',
    styleUrls: ['./seat-selection.component.scss']
})
export class SeatSelectionComponent implements OnInit{
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

    ngOnInit(): void {
        this.seatForm = this.formBuilder.group({
            items: this.formBuilder.array([])
        });
        this.createFormArray(3);
    }

    submit() {
        if (this.seatForm.invalid) {
            return;
        }
        this.seatSelected.emit(this.seatForm.value);
    }

    get formArray(): FormArray {
        return (this.seatForm.controls.items as FormArray);
    }

    get forms(): AbstractControl[] {
        return this.formArray.controls;
    }

    createFormArray(count: number) {
        for (let i = 0; i < count; i++) {
            this.appendSeat();
        }
    }

    appendSeat() {
        this.formArray.push(this.formBuilder.group({
            row: ['', Validators.required],
            column: ['', Validators.required],
        }));
    }
}
