import {Component, OnInit} from '@angular/core';

@Component({
    selector: 'app-time-tracking',
    templateUrl: './time-tracking.component.html',
    styleUrls: ['./time-tracking.component.scss']
})
export class TimeTrackingComponent implements OnInit {
    elapsedTime: number = 0;
    timer: any;

    constructor() {
    }

    ngOnInit() {
    }

    start() {
        this.timer = setInterval(this.updateTime, 1000);
    }

    stop() {
        clearInterval(this.timer);
    }

    reset() {
        this.elapsedTime = 0;
    }

    updateTime = () => {
        this.elapsedTime++;
    }
}
