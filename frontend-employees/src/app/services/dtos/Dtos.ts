export interface FlightDto {
    id: number;
    start: string;
    destination: string;
    delay: number;
    startTime: Date;
    startTimezone: string;
    detinationTimezone: string;
    arrivalTime: Date;
    realStartTime: Date;
    realLandingTime: Date;
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
    flightID: number;
    flightDelayed: boolean;
    notEnoughPersonel: boolean;
    planeNotAvailable: boolean;
    planeNotAvailableFixable: boolean;
    reservePlanes: PlaneDto[];
}

export interface ReservePlaneSolutionDto{
    flightID: number;
    reservePlaneID: number;
}

export interface PlaneDto {
    id: number;
    type: string;
}

export enum ScheduleConfigSolution {
    CANCEL_FLIGHT,
    DO_NOTHING,
    USE_BACKUP_PLANE,
    USE_BACKUP_PERSONAL
}

export enum BaggageState {
    AT_TRAVELLER,
    ON_BAGGAGE_CAROUSEL,
    IN_LUGGAGE_HALL,
    ON_LUGGAGE_CART,
    ON_PLANE,
    READY_FOR_PICK_UP
}

export interface ChangeBaggageStateDto {
    id: number;
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

export interface VacationPlanDto {
    vacations: VacationDto[];
    remainingVacationDays: number;
}

export interface VacationDto {
    startDate: Date;
    endDate: Date;
}

export interface RequestVacationDto {
    startDate: Date;
    endDate: Date;
}

export interface PlaneMaintenance {
    id: number;
    state: string;
    flightHours: number;
}

export interface WorkingHoursDto {
    startTime: Date;
    endTime: Date;
}

export interface TrackingDto{
    action: "Start"| "Landung" | "Verspätung" | "Bereit";
    delay: number;
    date ?: Date;
}

export interface VacationRequestDeniedDto {
    msg: string;
}
