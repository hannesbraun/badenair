import {Component} from '@angular/core';

@Component({
    selector: 'app-time-tracking',
    templateUrl: './time-tracking.component.html',
    styleUrls: ['./time-tracking.component.scss']
})
export class TimeTrackingComponent {
    elapsedTime = 0;
    isTimerRunning = false;
    timer: any;

    constructor() {
    }

    start(): void {
        this.isTimerRunning = true;
        this.timer = setInterval(() =>
            this.elapsedTime++, 1000);
    }

    stop(): void {
        this.isTimerRunning = false;
        clearInterval(this.timer);
    }

    get time() {
        return this.elapsedTime * 1000;
    }
}
