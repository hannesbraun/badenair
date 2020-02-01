import {Component, OnInit} from '@angular/core';

@Component({
    selector: 'app-time-tracking',
    templateUrl: './time-tracking.component.html',
    styleUrls: ['./time-tracking.component.scss']
})
export class TimeTrackingComponent implements OnInit {
    elapsedTime = 0;
    timer: any;

    constructor() {
    }

    ngOnInit() {
    }

    start(): void {
        this.timer = setInterval(this.updateTime, 1000);
    }

    stop(): void {
        clearInterval(this.timer);
    }

    reset(): void {
        this.elapsedTime = 0;
    }

    updateTime = () => {
        this.elapsedTime++;
    }
}
