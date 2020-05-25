import { Component, OnInit } from '@angular/core';
import { FlightService } from 'src/app/services/flights/flight.service';
import { FlightDto, TrackingDto } from 'src/app/services/dtos/Dtos';

@Component({
    selector: 'app-start-landing',
    templateUrl: './start-landing.component.html',
    styleUrls: ['./start-landing.component.scss']
})
export class StartLandingComponent implements OnInit {
    isStarted = false;
    hasDelay = false;
    loaded = false;
    delayTransmitted = false;

    flight!: FlightDto;

    constructor(private flightService: FlightService) { }


    ngOnInit() {
        this.flightService.getCurrentFlightforPilot().subscribe(res => { this.flight = res },
            err => { },
            () => {
                this.flightService.getCurrentTrackingAction(this.flight.id).subscribe(res => {
                    if (res.action === "Start") {
                        this.isStarted = true
                        this.flight.startTime = res.date as Date;
                    }
                    else if (res.action === "Landung") {
                        this.isStarted = false
                        this.flight.arrivalTime = res.date as Date;
                    }
                },
                    err => { },
                    () => {
                        this.loaded = true;
                    });
            });
    }

    start(flight: FlightDto) {
        this.isStarted = true;
        this.flightService.updateFlightTracking(flight.id, { action: "Start" } as TrackingDto).subscribe((res) => flight.startTime = res as Date);
    }

    land(flight: FlightDto) {
        this.isStarted = false;
        this.flightService.updateFlightTracking(flight.id, { action: "Landung" } as TrackingDto).subscribe(res => flight.arrivalTime = res as Date);
    }

    delay(flight: FlightDto) {
        this.flightService.updateFlightTracking(flight.id, { action: "Verspätung", delay: 1100010 } as TrackingDto).subscribe();
        this.delayTransmitted= true;
    }

    /* get name() {
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
    } */
}
