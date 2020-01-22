import {Component, EventEmitter, Output} from '@angular/core';

export enum BookingState {
    NOT_BOOKED,
    BOOKED
}

const CHECK_ICON = 'check';
const CLOSED_ICON = 'closed';

@Component({
    selector: 'app-check-button',
    template: `
        <button mat-raised-button color="primary" *ngIf="isNotBooked()" (click)="onClickBookFlight()">
            Buchen
        </button>

        <button mat-raised-button class="checked" *ngIf="isBooked()" (click)="onClickCancelBooking()"
                (mouseenter)="onEnterButton()" (mouseleave)="onLeaveButton()">
            <mat-icon>{{icon}}</mat-icon>
        </button>
    `,
    styles: [`
        button {
            width: 100%;
        }

        .checked {
            color: white;
            background-color: #4CAF50;
        }

        .checked:hover {
            background-color: #FF5252;
        }
    `]
})
export class CheckButtonComponent {

    state = BookingState.NOT_BOOKED;
    icon = CHECK_ICON;

    @Output() bookingStateChanged = new EventEmitter<BookingState>();

    onClickBookFlight() {
        this.handleStateChange(BookingState.BOOKED);
    }

    onClickCancelBooking() {
        this.handleStateChange(BookingState.NOT_BOOKED);
    }

    onEnterButton() {
        this.icon = CLOSED_ICON;
    }

    onLeaveButton() {
        this.icon = CHECK_ICON;
    }

    isNotBooked(): boolean {
        return this.state === BookingState.NOT_BOOKED;
    }

    isBooked(): boolean {
        return this.state === BookingState.BOOKED;
    }

    private handleStateChange(newState: BookingState) {
        this.state = newState;
        this.bookingStateChanged.emit(newState);
    }
}
