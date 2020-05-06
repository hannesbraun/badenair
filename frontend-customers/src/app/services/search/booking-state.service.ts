import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {FlightDto, PassengerDto} from '../dtos/Dtos';
import {SearchFormValue} from '../../components/flight-search/flight-search.component';
import {Seat} from '../../components/seat-selection/seat-selection.component';

interface BookingState {
    searchValue: SearchFormValue;
    passengers: number;
    returnFlights: FlightDto[];
    toFlights: FlightDto[];
    selectedReturnFlight: FlightDto;
    selectedToFlight: FlightDto;
    direction: boolean;
    seatDirection: boolean;
    passengersDto: PassengerDto[];
    toSeats: Seat[];
    returnSeats: Seat[];
}

@Injectable({
    providedIn: 'root'
})
export class BookingStateService {
    private readonly bookingState: BehaviorSubject<BookingState>;

    constructor() {
        this.bookingState = new BehaviorSubject<BookingState>({} as BookingState);
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
            passengers: passengers,
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
            direction: direction,
        });
    }

    setPassengersDto(value: PassengerDto[]) {
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
