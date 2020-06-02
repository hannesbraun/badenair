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
            conflictOptionSelect: ['', Validators.required]});
    }

    onClickClose(event: Event) {
        event.preventDefault();
        this.dialog.close();
    }

    onSubmit() {
        const conflictOptionSelect = this.conflictForm.get('conflictOptionSelect');
        if (this.conflictForm.valid && conflictOptionSelect) {
            const output: ScheduleConflictDialogOutput = {
                selectedOption: conflictOptionSelect.value
            };
            this.dialog.close(output);
        }
    }

    get scheduleConfigSolutionOptions() {
        return ScheduleConfigSolution;
    }
}
