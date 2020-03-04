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

export interface ScheduleConflictDto {
    flight: FlightDto;
    cause: string;
}

export enum ScheduleConfigSolution {
    CANCEL_FLIGHT,
    DO_NOTHING,
    USE_BACKUP_PLANE
}

export interface ServiceScheduleDto {
    schedule: Schedule;
    employee: string;
}

export interface Schedule {
    start: Date;
    end: Date;
}
