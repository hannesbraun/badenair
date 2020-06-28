import {Component, Inject} from '@angular/core';
import {FlightDto, ScheduleConflictDto, ScheduleConfigSolution} from '../../../../services/dtos/Dtos';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ScheduleConflictDialogOutput } from '../schedule-conflict-dialog/schedule-conflict-dialog.component';

export interface FlightInfoDialogInput {
    plane: string;
    flight: FlightDto;
    conflict: ScheduleConflictDto;
}

@Component({
    selector: 'app-flight-info-dialog',
    templateUrl: './flight-info-dialog.component.html',
    styleUrls: ['./flight-info-dialog.component.scss']
})
export class FlightInfoDialogComponent {

    conflictForm: FormGroup;

    constructor(private dialog: MatDialogRef<FlightInfoDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: FlightInfoDialogInput, private fb: FormBuilder) {
        this.conflictForm = this.fb.group({
            conflictOptionSelect: ['', Validators.required],
            reservePlaneSelect: ['']});
    }

    onClickClose(event: Event) {
        event.preventDefault();
        this.dialog.close();
    }

    onSubmit() {
        const conflictOptionSelect = this.conflictForm.get('conflictOptionSelect');
        const reservePlaneSelect = this.conflictForm.get('reservePlaneSelect');
        if (this.conflictForm.valid && conflictOptionSelect) {
            const output: ScheduleConflictDialogOutput = {
                selectedOption: conflictOptionSelect.value,
                reservePlane: reservePlaneSelect?.value
            };
            this.dialog.close(output);
        }
    }

    getHighlightAcceptDelay(flight: FlightDto, conflict: ScheduleConflictDto) : boolean{
        if (conflict.planeNotAvailable)
            return false;
        
        if (flight.start === "Karlsruhe/Baden-Baden"){
            if (flight.delay <= 15)
                return true;
            else
                return false;
        }
        else {
            return !conflict.planeNotAvailable;
        }
    }

    getHighlightReservePlane(flight: FlightDto, conflict: ScheduleConflictDto) : boolean{
        if (!conflict.planeNotAvailableFixable)
            return false;
        
        if (flight.start === "Karlsruhe/Baden-Baden"){
            if (flight.delay > 15)
                return true;
            else
                return false;
        }
        else {
            return false;
        }
    }

    getHighlightCancelFlight(flight: FlightDto, conflict: ScheduleConflictDto) : boolean{
        if (this.isDelayOverrun())
            return true;

        if (this.getHighlightAcceptDelay(flight, conflict) == false && this.getHighlightReservePlane(flight, conflict) == false)
            return true;
        else
            return false;
    }

    isCancelFlightOption() : boolean{
        if (this.isDelayOverrun())
            return true;

        if (this.data.flight.realStartTime)
            return false;
        if (this.data.flight.start === "Karlsruhe/Baden-Baden")
            return true;
        return false;
    }

    isAcceptDelayOption() : boolean{
        if (this.isDelayOverrun())
            return false;
        
        return true;
    }

    isUseReservePlaneOption() : boolean{
        if (this.isDelayOverrun())
            return false;

        if (this.data.flight.realStartTime)
            return false;
        if (this.data.flight.start === "Karlsruhe/Baden-Baden"){
            if (this.data.conflict.planeNotAvailableFixable){
                return true;
            }
        }
        return false;
    }

    get scheduleConfigSolutionOptions() {
        return ScheduleConfigSolution;
    }

    isDelayOverrun(){
        let now = new Date;

        if (now.getHours() > 6)
            now.setDate(now.getDate() + 1);
        now.setHours(6);
        now.setMinutes(0);
        now.setSeconds(0);
        
        let delayHours = this.data.flight.arrivalTime.getHours() + this.data.flight.delay / 60;
        let delayMinutes = this.data.flight.arrivalTime.getMinutes() + this.data.flight.delay % 60;
        let delayedDate = now;

        if (delayMinutes > 59){
            delayHours++;
            delayMinutes -= 60;
        }
        if (delayHours > 23){
            delayHours -= 24;
        }
        else
            delayedDate.setDate(delayedDate.getDate() - 1);

        delayedDate.setHours(delayHours);
        delayedDate.setMinutes(delayMinutes);

        if (delayedDate > now)
            return true;
        return false;
    }
}
