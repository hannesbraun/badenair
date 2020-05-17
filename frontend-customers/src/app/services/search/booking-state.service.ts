import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {FlightDto, TravelerDto} from '../dtos/Dtos';
import {SearchFormValue} from '../../components/flight-search/flight-search.component';
import {Seat} from '../../components/seat-selection/seat-selection.component';
import {AuthService} from '../../auth/auth.service';

interface BookingState {
    searchValue: SearchFormValue;
    passengers: number;
    returnFlights: FlightDto[];
    toFlights: FlightDto[];
    selectedReturnFlight: FlightDto;
    selectedToFlight: FlightDto;
    direction: boolean;
    seatDirection: boolean;
    passengersDto: TravelerDto[];
    toSeats: Seat[];
    returnSeats: Seat[];
}

@Injectable({
    providedIn: 'root'
})
export class BookingStateService {

    public static readonly BOOKING_STATE_KEY = 'bookingState';

    private readonly bookingState: BehaviorSubject<BookingState>;

    constructor(authService: AuthService) {
        const currentState = authService.getData<BookingState>(BookingStateService.BOOKING_STATE_KEY) ?? {} as BookingState;
        this.bookingState = new BehaviorSubject<BookingState>(currentState);
    }

    setReturnFlights(flights: FlightDto[]) {
        this.bookingState.next({
            ...this.bookingState.getValue(),
            returnFlights: flights,
        });
    }

    setToFlights(flights: FlightDto[]) {
        this.bookingState.next({
            ...this.bookingState.getValue(),
            toFlights: flights,
        });
    }

    setSelectedReturnFlight(flight: FlightDto) {
        this.bookingState.next({
            ...this.bookingState.getValue(),
            selectedReturnFlight: flight,
        });
    }

    setSelectedToFlight(flight: FlightDto) {
        this.bookingState.next({
            ...this.bookingState.getValue(),
            selectedToFlight: flight,
        });
    }

    setPassengers(passengers: number) {
        this.bookingState.next({
            ...this.bookingState.getValue(),
            passengers,
        });
    }

    setSearchValue(value: SearchFormValue) {
        this.bookingState.next({
            ...this.bookingState.getValue(),
            searchValue: value,
            direction: true,
        });
    }

    setDirection(direction: boolean) {
        this.bookingState.next({
            ...this.bookingState.getValue(),
            direction,
        });
    }

    setPassengersDto(value: TravelerDto[]) {
        this.state.next({
            ...this.bookingState.getValue(),
            passengersDto: value,
        });
    }

    setToSeats(value: Seat[]) {
        this.state.next({
            ...this.bookingState.getValue(),
            toSeats: value,
        });
    }

    setReturnSeats(value: Seat[]) {
        this.state.next({
            ...this.bookingState.getValue(),
            returnSeats: value,
        });
    }

    setSeatDirection(direction: boolean) {
        this.bookingState.next({
            ...this.bookingState.getValue(),
            seatDirection: direction,
        });
    }

    get state() {
        return this.bookingState;
    }
}
