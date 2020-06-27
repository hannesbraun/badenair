import {Component, Input, OnInit} from '@angular/core';
import {
    calculateDurationLength,
    calculateHeight,
    calculateRemainingLength,
    calculateStart,
    calculateRealStart,
    calculateRealDurationLength,
    displayableHours,
    getViewBoxConfig,
    hourWidth,
    lineHeight,
    totalWidth
} from '../../services/util/SVGUtil';
import {FlightDto, PlaneScheduleDto, ScheduleConflictDto, ScheduleConfigSolution, ReservePlaneSolutionDto} from '../../services/dtos/Dtos';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {
    FlightInfoDialogComponent,
    FlightInfoDialogInput
} from './dialogs/flight-info-dialog/flight-info-dialog.component';
import {FlightService} from '../../services/flights/flight.service';
import {timer} from 'rxjs';
import { ConflictService } from 'src/app/services/flights/conflict.service';
import {InfoService} from '../../services/info/info.service';
import { ScheduleConflictDialogOutput } from './dialogs/schedule-conflict-dialog/schedule-conflict-dialog.component';

interface LengthData {
    start: number;
    duration: number;
    realStart: number;
    realDuration: number;
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

    drawView: boolean = true;

    constructor(private dialog: MatDialog,
                private conflictService: ConflictService,
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
                                realStart: calculateRealStart(flight),
                                realDuration: calculateRealDurationLength(flight)
                            };
                        });
                    });

                this.schedules.forEach((schedule: PlaneScheduleDto) => {
                    if (schedule.hasConflict) {
                        needToUpdateConflicts = true;
                    }
                });


                if (needToUpdateConflicts) {
                    this.conflictService.getConflicts()
                        .subscribe(conflicts => {
                            this.conflicts = conflicts;
                        });
                }
            }, error => this.infoService.showErrorMessage('Der Flugplan konnte nicht abgerufen werden'));

        this.redrawView();
    }

    getTimeToDisplay(i: number): string {
        let hours = new Date().getHours() + i - 1;
        hours = 5 + i;

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
        const conflictDto = this.conflicts.find(value => (flight.id === value.flightID));
        const config: MatDialogConfig = {
            data: {
                plane,
                flight,
                conflict: conflictDto
            } as FlightInfoDialogInput
        };
        this.dialog.open(FlightInfoDialogComponent, config).afterClosed().subscribe((value: ScheduleConflictDialogOutput) => {
            if (value){
                if (value.selectedOption == ScheduleConfigSolution.USE_BACKUP_PLANE){
                    if (!value.reservePlane)
                        return;

                    let data: ReservePlaneSolutionDto={
                        flightID: flight.id,
                        reservePlaneID: value.reservePlane
                    }
                    this.conflictService.useReservePlane(data).subscribe();
                }

                else if (value.selectedOption == ScheduleConfigSolution.CANCEL_FLIGHT){
                    this.conflictService.cancelFlight(flight.id).subscribe();
                }

                else if (value.selectedOption == ScheduleConfigSolution.DO_NOTHING){
                    this.conflictService.ignoreDelay(flight.id).subscribe();
                }
                this.updateFlightPlan();
            }
        });
    }

    checkFlightForConflict(flight: FlightDto) {
        if (this.conflicts.find(value => (flight.id === value.flightID)))
            return true;
        return false;
    }

    getRemainingLength() : number{
        return calculateRemainingLength();
    }

    /*
    onClickConflict(id: number) {
        const conflictDto = this.conflicts.find(value => (id === value.flightID));
        var flightDto;

        for (var i = 0; i < this.schedules.length; i++){
            for (var j = 0; j < this.schedules[i].flights.length; j++){
                if (this.schedules[i].flights[j].id === id)
                    flightDto = this.schedules[i].flights[j];
            }
        }
        
        const config: MatDialogConfig = {
            data: {
                conflict: conflictDto,
                flight: flightDto
            } as ScheduleConflictDialogInput
        };
        this.dialog.open(ScheduleConflictDialogComponent, config).afterClosed().subscribe(output => {
            if (output) {
                // TODO: Handle selected solution and refresh overview
                console.log(output);
            }
        });
    }
    */

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

    redrawView() {
        this.drawView = false;
        this.drawView = true;
    }
}
