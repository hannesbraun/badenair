import {FlightDto} from '../dtos/Dtos';

export const displayableHours: number[] = [...Array(23).keys()].map(value => value + 1);
export const lineHeight = 20;
export const hourWidth = 15;
export const totalWidth = 550;

export function calculateStart(flight: FlightDto): number {
    let time = flight.startTime.getHours() + flight.startTime.getMinutes() / 60;
    if (time < 6)
        time += 24;
    const start = 205 + (time - 6) * hourWidth;

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
    if (new Date().getTime() > flight.arrivalTime.getTime())
        return calculateDurationLength(flight);

    if (new Date().getTime() < flight.startTime.getTime())
        return 0;

    else
        return toHours(new Date().getTime() - flight.startTime.getTime())* hourWidth;
}

export function calculateRealStart(flight: FlightDto): number {
    if (!flight.realStartTime)
        return calculateStart(flight);
    
    const time = flight.realStartTime.getHours() + flight.realStartTime.getMinutes() / 60;
    const start = 205 + (time - 6) * hourWidth;

    if (start < 190) {
        return 190;
    }

    return start;
}

export function calculateRealDurationLength(flight: FlightDto): number {
    if (!flight.realStartTime)
        return 0;
    if (!flight.realLandingTime)
        return calculateDurationLength(flight);

    const duration = toHours(flight.realLandingTime.getTime() - flight.realStartTime.getTime());

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
