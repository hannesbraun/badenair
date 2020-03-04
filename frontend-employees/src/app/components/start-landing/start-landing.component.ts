import {Component} from '@angular/core';

interface Plane {
    name: string;
    pilotName: string;
    startTime: Date;
    landingTime: Date;
}

@Component({
    selector: 'app-start-landing',
    templateUrl: './start-landing.component.html',
    styleUrls: ['./start-landing.component.scss']
})
export class StartLandingComponent {
    isStarted = false;
    hasDelay = false;
    private plane = {
        name: 'Flugzeug 3',
        pilotName: 'Max Musterman'
    } as Plane;

    start() {
        this.isStarted = true;
        this.plane.startTime = new Date();
    }

    land() {
        this.isStarted = false;
        this.plane.landingTime = new Date();
    }

    get name() {
        return this.plane.pilotName;
    }

    get planeName() {
        return this.plane.name;
    }

    get startTime(): Date {
        return this.plane.startTime;
    }

    get landingTime(): Date {
        return this.plane.landingTime;
    }
}
