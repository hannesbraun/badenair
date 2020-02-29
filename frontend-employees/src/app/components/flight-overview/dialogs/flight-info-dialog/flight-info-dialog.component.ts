import {Component, Inject} from '@angular/core';
import {FlightDto} from '../../../../services/dtos/Dtos';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';

export interface FlightInfoDialogInput {
    plane: string;
    flight: FlightDto;
}

@Component({
    selector: 'app-flight-info-dialog',
    templateUrl: './flight-info-dialog.component.html',
    styleUrls: ['./flight-info-dialog.component.scss']
})
export class FlightInfoDialogComponent {

    constructor(private dialog: MatDialogRef<FlightInfoDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: FlightInfoDialogInput) {
    }

    onClickClose() {
        this.dialog.close();
    }
}
