import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {BookingStateService} from '../../services/search/booking-state.service';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {Seat} from '../../components/seat-selection/seat-selection.component';
import {SeatService} from '../../services/seat/seat.service';
import {SeatDto} from '../../services/dtos/Dtos';

@Component({
    selector: 'app-seat-selection-page',
    templateUrl: './seat-selection-page.component.html',
    styleUrls: ['./seat-selection-page.component.scss']
})
export class SeatSelectionPageComponent implements OnInit {

    passengers = new Observable<number>();
    selectedSeats = new Observable<Seat[]>();
    seats = new Observable<SeatDto>();

    constructor(
        private route: Router,
        private bookingStateService: BookingStateService,
        private seatService: SeatService,
    ) {
    }

    ngOnInit(): void {
        this.passengers = this.bookingStateService.state.pipe(
            map(state => state.passengers)
        );
        this.selectedSeats = this.bookingStateService.state.pipe(
            map(state => state.seats)
        );
        this.bookingStateService.state.subscribe(state => {
            this.seats = this.seatService.getSeats(state.selectedToFlight.id);
        });
    }

    seatSelected(value: Seat[]) {
        this.bookingStateService.setSeats(value);
        this.route.navigate(['overview']);
    }
}
