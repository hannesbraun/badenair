import {Injectable} from '@angular/core';
import {BehaviorSubject, ReplaySubject} from 'rxjs';
import {FlightDto} from '../dtos/Dtos';

interface BookingState {
    searchValue: any;
    passengers: number;
    returnFlights: FlightDto[];
    toFlights: FlightDto[];
    selectedReturnFlight: FlightDto;
    selectedToFlight: FlightDto;
}

@Injectable({
    providedIn: 'root'
})
export class BookingStateService {

    private readonly bookingState: BehaviorSubject<BookingState>;


    private readonly searchData: ReplaySubject<FlightDto[]>;


    constructor() {
        this.searchData = new ReplaySubject<FlightDto[]>(1);
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

    setSelectedReturnFlights(flight: FlightDto) {
        this.bookingState.next({
            ...this.bookingState.getValue(),
            selectedReturnFlight: flight,
        });
    }

    setSelectedToFlights(flight: FlightDto) {
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

    setSearchValue(value: any) {
        this.bookingState.next({
            ...this.bookingState.getValue(),
            searchValue: value,
        });
    }

    get state() {
        return this.bookingState;
    }

    setFlightSearchData(data: FlightDto[]) {
        this.searchData.next(data);
    }

    get flightSearchData() {
        return this.searchData;
    }
}
