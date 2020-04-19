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
}

export type AccountData = Partial<Omit<SignUpDto, 'password' | 'email'>>;

export interface UpdateProfileDto {
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
}

export interface FlightDto {
    id: number;
    start: string;
    destination: string;
    startTime: Date;
    arrivalTime: Date;
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
