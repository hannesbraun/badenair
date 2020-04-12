export interface FlightDto {
    id: number;
    start: string;
    destination: string;
    startTime: Date;
    arrivalTime: Date;
}

export enum UserType {
    pilot,
    technician,
    ground,
    flightDirector
}

export interface User {
    type: UserType;
    name: string;
}

export interface PlaneScheduleDto {
    id: number;
    plane: string;
    status: string;
    hasConflict: boolean;
    flights: FlightDto[];
}

export interface ScheduleConflictDto {
    flight: FlightDto;
    cause: string;
}

export enum ScheduleConfigSolution {
    CANCEL_FLIGHT,
    DO_NOTHING,
    USE_BACKUP_PLANE
}

export enum BaggageState {
    ON_FLIGHT,
    READY_TO_GET
}

export interface ChangeBaggageStateDto {
    flightId: number;
    baggageId: string;
    state: BaggageState;
}

export interface ServiceScheduleDto {
    schedule: Schedule;
    employee: string;
}

export interface Schedule {
    start: Date;
    end: Date;
}

export interface VacationDto {
    startDate: Date;
    endDate: Date;
}

export interface RequestVacationDto {
    startDate: Date;
    endDate: Date;
}
