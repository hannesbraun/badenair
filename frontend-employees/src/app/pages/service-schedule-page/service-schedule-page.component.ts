import {Component, OnInit} from '@angular/core';
import {ServiceScheduleDto} from '../../services/dtos/Dtos';
import {ServiceScheduleService} from '../../services/service-schedule/service-schedule.service';

@Component({
    selector: 'app-service-schedule-page',
    templateUrl: './service-schedule-page.component.html',
    styleUrls: ['./service-schedule-page.component.scss']
})
export class ServiceSchedulePageComponent implements OnInit {

    serviceScheduleData: ServiceScheduleDto[] | undefined;
    displayedColumns = ['scheduledTime', 'name'];

    constructor(private serviceScheduleService: ServiceScheduleService) {
    }

    ngOnInit(): void {
        this.serviceScheduleService.getServiceSchedule().subscribe(dto => {
            this.serviceScheduleData = dto;
        });
    }

}
