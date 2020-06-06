import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FlightDto } from '../../services/dtos/Dtos';
import { formatDuration } from '../../services/util/DurationFormatter';

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

    @Output() booking = new EventEmitter<FlightDto>();
    @Output() onCheckInButtonClick = new EventEmitter<FlightDto>();

    onBooking() {
        this.booking.emit(this.flight);
    }

    checkIn() {
        this.onCheckInButtonClick.emit(this.flight);
    }

    getDuration() {
        return formatDuration(this.flight.arrivalTime.getTime() - this.flight.startTime.getTime());
    }
}
