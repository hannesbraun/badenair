import {Component, OnDestroy, OnInit} from '@angular/core';
import {BookingStateService} from '../../services/search/booking-state.service';
import {Subscription} from 'rxjs';
import {BookingDto, FlightDto, TravelerDto} from 'src/app/services/dtos/Dtos';
import {BookingService} from 'src/app/services/booking/booking.service';
import {Router} from '@angular/router';
import {Seat} from 'src/app/components/seat-selection/seat-selection.component';
import {InfoService} from '../../services/info/info.service';


@Component({
    selector: 'app-booking-overview-page',
    templateUrl: './booking-overview-page.component.html',
    styleUrls: ['./booking-overview-page.component.scss']
})
export class BookingOverviewPageComponent implements OnInit, OnDestroy {
    private bookingStateSubscription!: Subscription;
    toFlight!: FlightDto;
    returnFlight !: FlightDto;
    passengers!: TravelerDto[];
    toSeats !: Seat[];
    returnSeats !: Seat[];
    price = 0;
    baggagePrice = 2;

    constructor(private bookingStateService: BookingStateService,
                private bookingService: BookingService,
                private router: Router,
                private infoService: InfoService) {
    }

    ngOnInit(): void {
        this.passengers = [];
        this.toSeats = [];
        this.returnSeats = [];
        this.bookingStateSubscription = this.bookingStateService.state
            .subscribe(state => {
                if (state.selectedToFlight) {
                    this.toFlight = state.selectedToFlight;
                }

                if (state.selectedReturnFlight) {
                    this.returnFlight = state.selectedReturnFlight;
                }

                if (state.passengers > 0) {
                    state.passengersDto.forEach(passenger => {
                        this.passengers.push(passenger);
                    });
                }

                if (state.toSeats && state.toSeats.length > 0) {
                    state.toSeats.forEach(seat => {
                        this.toSeats.push(seat);
                    });
                }
                if (state.returnSeats && (state.returnSeats.length > 0)) {
                    state.returnSeats.forEach(seat => {
                        this.returnSeats.push(seat);
                    });
                    state.toSeats.forEach(seat => {
                        this.toSeats.push(seat);
                    });
                }

                this.price = this.calculatePrice(this.toFlight) + this.calculatePrice(this.returnFlight);
            });
    }

    ngOnDestroy(): void {
        this.bookingStateSubscription.unsubscribe();
    }

    bookFlights() {
        const bookingIds: number[] = [];

        if (this.toFlight && this.returnFlight && (this.passengers.length > 0)) {
            // Book to flight
            this.bookingService.bookFlight({
                flightId: this.toFlight.id,
                passengers: this.passengers,
                seats: this.toSeats,
                price: this.calculatePrice(this.toFlight)
            } as BookingDto).subscribe(id => {
                bookingIds.push(id);

                // Book return flight
                this.bookingService.bookFlight({
                    flightId: this.returnFlight.id,
                    passengers: this.passengers,
                    seats: this.returnSeats,
                    price: this.calculatePrice(this.returnFlight)
                } as BookingDto).subscribe(id => {
                    bookingIds.push(id);
                    this.confirmBooking(bookingIds);
                }, this.onError);

            }, this.onError);

        } else if (this.toFlight && (this.passengers.length > 0)) {
            // Book flight
            this.bookingService.bookFlight({
                flightId: this.toFlight.id,
                passengers: this.passengers,
                seats: this.toSeats,
                price: this.calculatePrice(this.toFlight)
            } as BookingDto).subscribe(id => {
                bookingIds.push(id);
                this.confirmBooking(bookingIds);
            }, this.onError);

        } else {
            this.onError();
        }
    }

    private calculatePrice(flight: FlightDto): number {
        let price = 0;

        if (flight) {
            price += flight.price * this.passengers.length;

            this.passengers.forEach(passenger => {
                price += passenger.baggage1 * this.baggagePrice;
                price += passenger.baggage2 * this.baggagePrice;
                price += passenger.baggage3 * this.baggagePrice;
                price += passenger.baggage4 * this.baggagePrice;
            });
        }
        return price;
    }

    private confirmBooking(bookingIds: number[]) {
        // Confirm booking
        this.bookingService.confirmBooking(bookingIds)
            .subscribe(() => this.router.navigate(['/success']),
                this.onError);
    }

    private onError = () => this.infoService.showErrorMessage('Die Buchung ist fehlgeschlagen.');
}
