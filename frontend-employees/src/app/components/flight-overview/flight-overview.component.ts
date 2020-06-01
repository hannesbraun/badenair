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
import {FlightDto, PlaneScheduleDto, ScheduleConflictDto} from '../../services/dtos/Dtos';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {
    FlightInfoDialogComponent,
    FlightInfoDialogInput
} from './dialogs/flight-info-dialog/flight-info-dialog.component';
import {
    ScheduleConflictDialogComponent,
    ScheduleConflictDialogInput
} from './dialogs/schedule-conflict-dialog/schedule-conflict-dialog.component';
import {ScheduleConflictService} from '../../services/conflicts/schedule-conflict-service.service';
import {FlightService} from '../../services/flights/flight.service';
import {timer} from 'rxjs';
import {InfoService} from '../../services/info/info.service';

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
    conflicts: ScheduleConflictDto[] = [];

    calculatedLengths: LengthData[][] = [];

    constructor(private dialog: MatDialog,
                private scheduleConflictServiceService: ScheduleConflictService,
                private flightService: FlightService,
                private infoService: InfoService) {
    }

    ngOnInit() {
        // TODO remove subscription to Service

        const refreshTimer = timer(0, 120000);
        refreshTimer.subscribe(this.updateFlightPlan);
    }

    updateFlightPlan = () => {
        let needToUpdateConflicts = false;

        this.flightService.getPlaneSchedules()
            .subscribe(schedules => {
                this.schedules = schedules;

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

                this.schedules.forEach((schedule: PlaneScheduleDto) => {
                    if (schedule.hasConflict) {
                        needToUpdateConflicts = true;
                    }
                });


                if (needToUpdateConflicts) {
                    this.scheduleConflictServiceService.getConflicts()
                        .subscribe(conflicts => {
                            this.conflicts = conflicts;
                        });
                }
            }, error => this.infoService.showErrorMessage('Der Flugplan konnte nicht agerufen werden'));

        this.flightService.getPlaneSchedules();
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

    onClickFlight(flight: FlightDto, plane: string) {
        const config: MatDialogConfig = {
            data: {
                plane,
                flight
            } as FlightInfoDialogInput
        };
        this.dialog.open(FlightInfoDialogComponent, config);
    }

    onClickConflict(id: number) {
        const conflictDto = this.conflicts.find(value => (id === value.scheduleId));

        const config: MatDialogConfig = {
            data: {
                conflict: conflictDto
            } as ScheduleConflictDialogInput
        };
        this.dialog.open(ScheduleConflictDialogComponent, config).afterClosed().subscribe(output => {
            if (output) {
                // TODO: Handle selected solution and refresh overview
                console.log(output);
            }
        });
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
