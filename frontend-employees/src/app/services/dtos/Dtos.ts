export interface FlightDto {
    start: string;
    destination: string;
    startTime: Date;
    arrivalTime: Date;
}

export interface PlaneScheduleDto {
    id: number;
    plane: string;
    status: string;
    hasConflict: boolean;
    flights: FlightDto[];
}
