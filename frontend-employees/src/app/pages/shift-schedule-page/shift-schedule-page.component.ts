import {Component, OnInit} from '@angular/core';
import {Schedule} from '../../services/dtos/Dtos';
import {ShiftScheduleService} from '../../services/shift-schedule/shift-schedule.service';

@Component({
    selector: 'app-shift-schedule-page',
    templateUrl: './shift-schedule-page.component.html',
    styleUrls: ['./shift-schedule-page.component.scss']
})
export class ShiftSchedulePageComponent implements OnInit {

    shiftScheduleData: Schedule[] | undefined;
    displayedColumns = ['shiftDate', 'start', 'end'];

    constructor(private shiftScheduleService: ShiftScheduleService) {
    }

    ngOnInit(): void {
        this.shiftScheduleService.getScheduleForEmployee(1).subscribe(dto => {
            this.shiftScheduleData = dto;
        });
    }
}
