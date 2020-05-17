import {Seat} from 'src/app/components/seat-selection/seat-selection.component';

export interface SignUpDto {
    lastname: string;
    name: string;
    birthDate: Date;
    street: string;
    zipCode: string;
    placeOfResidence: string;
    cardOwner: string;
    cardNumber: string;
    check: string;
    invalidationDate: Date;
    email: string;
    password: string;

    [key: string]: string | Date;
}

export type UpdateAccountDataDto = Omit<SignUpDto, 'password' | 'email' | 'lastname' | 'name'>;
export type AccountData = Partial<UpdateAccountDataDto>;

export interface FlightDto {
    id: number;
    start: string;
    destination: string;
    startTime: Date;
    arrivalTime: Date;
    price: number;
}

export interface TravelerDto {
    id: number;
    name: string;
    surname: string;
    checkedIn: boolean;
    baggage1: number;
    baggage2: number;
    baggage3: number;
    baggage4: number;
    seatRow: number;
    seatColumn: number;
}

export interface AirportDto {
    id: number;
    name: string;
}

export interface SeatDto {
    type: string;
    freeSeats: boolean[][];
}

export interface BookingDto {
    flightId: number;
    passengers: TravelerDto[];
    seats: Seat[];
    price: number;
}

export interface CheckInInfoDto {
    travelers: TravelerDto[];
    flight: FlightDto;
}
