import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {AbstractControl, FormArray, FormBuilder, Validators} from '@angular/forms';
import {Observable, Subscription} from 'rxjs';
import {SeatDto} from '../../services/dtos/Dtos';

export interface Seat {
    row: number;
    column: number;
}

@Component({
    selector: 'app-seat-selection',
    templateUrl: './seat-selection.component.html',
    styleUrls: ['./seat-selection.component.scss']
})
export class SeatSelectionComponent implements OnInit, OnChanges {
    columns = ['A', 'B', 'C', 'D', 'E', 'F'];
    planeType = '';
    seats: boolean[][] = [];
    freeSeatSubscription: Subscription;

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
        this.freeSeatSubscription = this.freeSeats.subscribe(seats => {
            this.seats = seats.freeSeats;
            this.planeType = seats.type;
            this.freeSeatSubscription.unsubscribe();
        });
    }

    ngOnChanges(changes: SimpleChanges) {
        if (changes.freeSeats) {
            this.freeSeatSubscription = this.freeSeats.subscribe(seats => {
                this.seats = seats.freeSeats;
                this.planeType = seats.type;
                this.freeSeatSubscription.unsubscribe();
            });
        }
    }

    submit() {
        if (this.seatForm.invalid) {
            return;
        }
        const value: Seat[] = Object.values(this.seatForm.controls.items.value);
        this.seatSelected.emit(value);
        this.formArray.reset();
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

    isRowDisabled(index: number): boolean {
        if (!this.seats) {
            return false;
        }
        return this.seats[index].every(value => !value);
    }

    isColumnDisabled(passengerIndex: number, index: number): boolean {
        if (!this.seats) {
            return true;
        }
        const rowValue = this.formArray.controls[passengerIndex]?.get('row')?.value;
        if (rowValue === undefined || rowValue === '' || rowValue === null) {
            return true;
        }
        return !this.seats[rowValue][index];
    }
}
