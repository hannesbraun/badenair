import {Component, OnInit} from '@angular/core';

@Component({
    selector: 'app-time-tracking',
    templateUrl: './time-tracking.component.html',
    styleUrls: ['./time-tracking.component.scss']
})
export class TimeTrackingComponent implements OnInit {
    elapsedTime = 0;
    isTimerRunning = false;
    timer: any;

    constructor() {
    }

    ngOnInit() {
    }

    start(): void {
        this.isTimerRunning = true;
        this.timer = setInterval(this.updateTime, 1000);
    }

    stop(): void {
        this.isTimerRunning = false;
        clearInterval(this.timer);
    }

    reset(): void {
        this.elapsedTime = 0;
    }

    updateTime = () => {
        this.elapsedTime++;
    }
}
