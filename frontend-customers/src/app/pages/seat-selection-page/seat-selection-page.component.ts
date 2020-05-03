import {Component, OnDestroy, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {BookingStateService} from '../../services/search/booking-state.service';
import {Observable, Subscription} from 'rxjs';
import {map} from 'rxjs/operators';
import {Seat} from '../../components/seat-selection/seat-selection.component';
import {SeatService} from '../../services/seat/seat.service';
import {SeatDto} from '../../services/dtos/Dtos';

@Component({
    selector: 'app-seat-selection-page',
    templateUrl: './seat-selection-page.component.html',
    styleUrls: ['./seat-selection-page.component.scss']
})
export class SeatSelectionPageComponent implements OnInit, OnDestroy {

    passengers = new Observable<number>();
    selectedSeats = new Observable<Seat[]>();
    seats = new Observable<SeatDto>();
    directionState = true;

    private type = '1';
    private bookingStateSubscription !: Subscription;

    constructor(
        private router: Router,
        private bookingStateService: BookingStateService,
        private seatService: SeatService,
    ) {
    }

    ngOnInit(): void {
        this.passengers = this.bookingStateService.state.pipe(
            map(state => state.passengers)
        );
        this.selectedSeats = this.bookingStateService.state.pipe(
            map(state => {
                if (this.directionState) {
                    return state.toSeats;
                } else {
                    return state.returnSeats;
                }
            })
        );
        this.bookingStateSubscription = this.bookingStateService.state
            .subscribe(state => {
                this.type = state.searchValue.type;

                if (state.seatDirection !== undefined) {
                    this.directionState = state.seatDirection;
                }

                if (this.directionState) {
                    this.seats = this.seatService.getSeats(state.selectedToFlight.id);
                } else {
                    this.seats = this.seatService.getSeats(state.selectedReturnFlight.id);
                }
            });
    }

    seatSelected(value: Seat[]) {
        if (this.directionState) {
            this.bookingStateService.setToSeats(value);
        } else {
            this.bookingStateService.setReturnSeats(value);
        }
        this.next();
    }

    next() {
        if (this.type === '2') {
            this.router.navigate(['/overview']);
            return;
        }
        if (this.directionState) {
            this.directionState = false;
            this.bookingStateService.setSeatDirection(false);
        } else {
            this.router.navigate(['/overview']);
        }
    }

    previous() {
        if (this.type === '2') {
            this.router.navigate(['/passengers']);
            return;
        }
        if (this.directionState) {
            this.router.navigate(['/passengers']);
        } else {
            this.directionState = true;
            this.bookingStateService.setSeatDirection(true);
        }
    }

    ngOnDestroy(): void {
        this.bookingStateSubscription.unsubscribe();
    }
}
