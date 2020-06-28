import { Component, OnInit, OnDestroy } from '@angular/core';
import { TimeTrackingService } from '../../services/time-tracking/time-tracking.service';
import { WorkingHoursDto } from 'src/app/services/dtos/Dtos';
import { InfoService } from '../../services/info/info.service';

@Component({
    selector: 'app-time-tracking',
    templateUrl: './time-tracking.component.html',
    styleUrls: ['./time-tracking.component.scss']
})
export class TimeTrackingComponent implements OnInit, OnDestroy {
    loaded = false;
    workingHours: WorkingHoursDto | undefined;
    now: Date = new Date();
    timer: any;

    constructor(private timeTrackingService: TimeTrackingService, private infoService: InfoService) {
    }

    ngOnInit(): void {
        this.timer = setInterval(() => {
            this.now = new Date();
        }, 1000);

        this.timeTrackingService.getLatestWorkingHours().subscribe(workingHours => {
            this.workingHours = workingHours || undefined;
            this.loaded = true;
        }, error => this.infoService.showErrorMessage('Ein unerwarteter Fehler ist aufgetreten'));
    }

    ngOnDestroy(): void {
        clearInterval(this.timer);
    }

    start(): void {
        this.timeTrackingService.triggerWorkingHours().subscribe(workingHours => {
            this.workingHours = workingHours;
        }, error => this.infoService.showErrorMessage('Ein unerwarteter Fehler ist aufgetreten'));
    }

    stop(): void {
        this.timeTrackingService.triggerWorkingHours().subscribe(workingHours => {
            this.workingHours = workingHours;
        }, error => this.infoService.showErrorMessage('Ein unerwarteter Fehler ist aufgetreten'));
    }

    get isTimerRunning() {
        return this.workingHours && this.workingHours.endTime === null;
    }

    get time() {
        if (this.workingHours) {
            if (this.isTimerRunning) {
                const diff = this.now.getTime() - this.workingHours.startTime.getTime();
                return diff < 0 ? 0 : diff;
            } else {
                return this.workingHours.endTime.getTime() - this.workingHours.startTime.getTime();
            }
        } else {
            return 0;
        }
    }
}
