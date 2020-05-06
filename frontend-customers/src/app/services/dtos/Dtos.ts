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

export interface PassengerDto {
    name: string;
    surname: string;
    checkedIn: boolean;
    baggage1: number;
    baggage2: number;
    baggage3: number;
    baggage4: number;
}

export interface AirportDto {
    id: number;
    name: string;
}

export interface SeatDto {
    type: string;
    freeSeats: boolean[][];
}
