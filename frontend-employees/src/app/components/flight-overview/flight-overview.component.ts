import {Component, Input, OnInit} from '@angular/core';
import {
    calculateDurationLength,
    calculateHeight,
    calculateRemainingLength,
    calculateStart,
    displayableHours,
    getViewBoxConfig,
    hourWidth,
    lineHeight,
    totalWidth
} from '../../services/util/SVGUtil';
import {FlightDto, PlaneScheduleDto} from '../../services/dtos/Dtos';

interface LengthData {
    start: number;
    duration: number;
    remaining: number;
}

@Component({
    selector: 'app-flight-overview',
    templateUrl: './flight-overview.component.html',
    styleUrls: ['./flight-overview.component.scss']
})
export class FlightOverviewComponent implements OnInit {

    @Input()
    schedules: PlaneScheduleDto[] = [];

    calculatedLengths: LengthData[][] = [];

    constructor() {
    }

    ngOnInit() {
        this.schedules
            .map(schedule => schedule.flights)
            .forEach((flights: FlightDto[], index: number) => {
                this.calculatedLengths[index] = flights.map(flight => {
                    return {
                        start: calculateStart(flight),
                        duration: calculateDurationLength(flight),
                        remaining: calculateRemainingLength(flight)
                    };
                });
            });
    }

    getTimeToDisplay(i: number): string {
        let hours = new Date().getHours() + i - 1;

        if (hours > 23) {
            hours -= 24;
        }

        let timeString = `${hours}:00`;

        if (timeString.length < 5) {
            timeString = `0${timeString}`;
        }

        return timeString;
    }

    get displayableHours(): number[] {
        return displayableHours;
    }

    get lineHeight(): number {
        return lineHeight;
    }

    get hoursWidth(): number {
        return hourWidth;
    }

    get totalWidth(): number {
        return totalWidth;
    }

    get totalHeight(): number {
        return calculateHeight(this.schedules.length);
    }

    get viewBoxConfig(): string {
        return getViewBoxConfig(this.totalHeight);
    }
}
