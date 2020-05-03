import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {AbstractControl, FormArray, FormBuilder, Validators} from '@angular/forms';
import {Observable} from 'rxjs';
import {SeatDto} from '../../services/dtos/Dtos';

export interface Seat {
    row: number;
    column: string;
}

@Component({
    selector: 'app-seat-selection',
    templateUrl: './seat-selection.component.html',
    styleUrls: ['./seat-selection.component.scss']
})
export class SeatSelectionComponent implements OnInit {
    columns = ['A', 'B', 'C', 'D', 'E', 'F'];
    planeType = '';
    seats: boolean[][] = [];

    seatForm = this.formBuilder.group({
        row: ['', Validators.required],
        column: ['', Validators.required],
    });

    @Input() freeSeats!: Observable<SeatDto>;
    @Input() selectedSeats!: Observable<Seat[]>;
    @Input() passengers!: Observable<number>;
    @Input() flightId!: number;
    @Output() seatSelected = new EventEmitter<Seat[]>();
    @Output() previous = new EventEmitter();

    constructor(private formBuilder: FormBuilder) {
    }

    ngOnInit(): void {
        this.seatForm = this.formBuilder.group({
            items: this.formBuilder.array([])
        });
        this.passengers.subscribe(count => {
            if (this.formArray.length === count) {
                return;
            }
            this.createFormArray(count);
        });
        this.selectedSeats.subscribe(seats => {
            if (!seats) {
                return;
            }
            this.seatForm.controls.items.patchValue(seats);
        });
        this.freeSeats.subscribe(seats => {
            this.seats = seats.freeSeats;
            this.planeType = seats.type;
        });
    }

    submit() {
        if (this.seatForm.invalid) {
            return;
        }
        const value: Seat[] = Object.values(this.seatForm.controls.items.value);
        this.seatSelected.emit(value);
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

    back() {
        this.previous.emit();
    }
}
