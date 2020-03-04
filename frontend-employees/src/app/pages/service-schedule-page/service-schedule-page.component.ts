import {Component, OnInit} from '@angular/core';

export interface ServiceScheduleDto {
    schedule: Schedule;
    employee: string;
}

export interface Schedule {
    start: Date;
    end: Date;
}

@Component({
    selector: 'app-service-schedule-page',
    templateUrl: './service-schedule-page.component.html',
    styleUrls: ['./service-schedule-page.component.scss']
})
export class ServiceSchedulePageComponent implements OnInit {

    dataSource: ServiceScheduleDto[] = [
        {
            schedule: {
                start: new Date(),
                end: new Date()
            },
            employee: 'Max Mustermann'
        },
        {
            schedule: {
                start: new Date(),
                end: new Date()
            },
            employee: 'Max Mustermann'
        },
        {
            schedule: {
                start: new Date(),
                end: new Date()
            },
            employee: 'Max Mustermann'
        },
        {
            schedule: {
                start: new Date(),
                end: new Date()
            },
            employee: 'Max Mustermann'
        }
    ];
    displayedColumns = ['scheduledTime', 'name'];

    constructor() {
    }

    ngOnInit(): void {
    }

}
