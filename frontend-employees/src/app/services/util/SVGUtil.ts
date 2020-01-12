import {FlightDto} from '../dtos/Dtos';

export const displayableHours: number[] = [...Array(13).keys()].map(value => value + 1);
export const lineHeight = 20;
export const hourWidth = 15;
export const totalWidth = 400;

export function calculateStart(flight: FlightDto): number {
    const time = flight.startTime.getHours() + flight.startTime.getMinutes() / 60;
    const start = 205 + (time - new Date().getHours()) * hourWidth;

    if (start < 190) {
        return 190;
    }

    return start;
}

export function calculateDurationLength(flight: FlightDto): number {
    const duration = toHours(flight.arrivalTime.getTime() - flight.startTime.getTime());
    return duration * hourWidth;
}

export function calculateRemainingLength(flight: FlightDto): number {
    const duration = toHours(new Date().getTime() - flight.startTime.getTime());

    if (duration < 0) {
        return 0;
    }

    return duration * hourWidth;
}

export function calculateHeight(numberOfFlights: number): number {
    return numberOfFlights * lineHeight + lineHeight;
}

export function getViewBoxConfig(totalHeight: number): string {
    return `0 0 ${totalWidth} ${totalHeight}`;
}

function toHours(dateInMilliseconds: number): number {
    return dateInMilliseconds / 1000 / 60 / 60;
}
