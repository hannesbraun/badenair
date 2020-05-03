import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FlightDto} from '../../services/dtos/Dtos';
import {BookingState} from './check-button/check-button.component';

export interface Person {
    name: string;
    id: number;
    checkedIn: boolean;
}

export interface Baggage {
    id: number;
    state: BaggageState;
}

export enum BaggageState {
    atTraveler = 'Beim Reisenden',
    onBaggageCarousel = 'Auf dem Gepäckband',
    inLuggageHall = 'In der Gepäckhalle',
    onLuggageCart = 'Auf dem Gepäckwagen',
    inPlane = 'Im Flugzeug',
    readyForPickUp = 'Bereit zum Abholen'
}

@Component({
    selector: 'app-flight',
    templateUrl: './flight.component.html',
    styleUrls: ['./flight.component.scss']
})
export class FlightComponent {

    @Input() numberOfPassengers !: number;
    @Input() price !: number;
    @Input() flight !: FlightDto;
    @Input() persons !: Person[];
    @Input() checkInState !: boolean;
    @Input() baggages !: Baggage[];

    @Output() bookingStateChanged = new EventEmitter<BookingState>();
    @Output() onCheckInButtonClick = new EventEmitter<FlightDto>();


    onBookingStateChanged(newState: BookingState) {
        this.bookingStateChanged.emit(newState);
    }

    checkIn() {
        this.onCheckInButtonClick.emit(this.flight);
    }

    getDuration() {
        return this.flight.arrivalTime.getTime() - this.flight.startTime.getTime();
    }
}
