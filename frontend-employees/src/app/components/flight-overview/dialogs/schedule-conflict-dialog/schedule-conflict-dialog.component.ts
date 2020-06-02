import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {ScheduleConfigSolution, ScheduleConflictDto, FlightDto} from '../../../../services/dtos/Dtos';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

export interface ScheduleConflictDialogInput {
    conflict: ScheduleConflictDto;
    flight: FlightDto;
}

export interface ScheduleConflictDialogOutput {
    selectedOption: ScheduleConfigSolution;
}

@Component({
    selector: 'app-schedule-conflict-dialog',
    templateUrl: './schedule-conflict-dialog.component.html',
    styleUrls: ['./schedule-conflict-dialog.component.scss']
})
export class ScheduleConflictDialogComponent {

    conflictForm: FormGroup;

    constructor(private dialog: MatDialogRef<ScheduleConflictDialogComponent>,
                @Inject(MAT_DIALOG_DATA) public data: ScheduleConflictDialogInput,
                private fb: FormBuilder) {
        this.conflictForm = this.fb.group({
            conflictOptionSelect: ['', Validators.required]
        });
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
