import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FlightDto} from '../../services/dtos/Dtos';
import {BookingState} from './check-button/check-button.component';

@Component({
    selector: 'app-flight',
    templateUrl: './flight.component.html',
    styleUrls: ['./flight.component.scss']
})
export class FlightComponent {

    @Input() numberOfPassengers !: number;
    @Input() price !: number;
    @Input() flight !: FlightDto;

    @Output() bookingStateChanged = new EventEmitter<BookingState>();

    onBookingStateChanged(newState: BookingState) {
        this.bookingStateChanged.emit(newState);
    }
}
