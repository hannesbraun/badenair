import { Component, OnInit } from '@angular/core';
import { FlightService } from 'src/app/services/flights/flight.service';
import { FlightDto, TrackingDto } from 'src/app/services/dtos/Dtos';
import { InfoService } from '../../services/info/info.service';

@Component({
    selector: 'app-start-landing',
    templateUrl: './start-landing.component.html',
    styleUrls: ['./start-landing.component.scss']
})
export class StartLandingComponent implements OnInit {
    isStarted = false;
    hasDelay = false;
    loaded = false;
    delayTime = 0;
    delayTransmitted = false;

    flight!: FlightDto;

    constructor(private flightService: FlightService, private infoService: InfoService) { }


    ngOnInit() {
        this.flightService.getCurrentFlightforPilot().subscribe(res => { this.flight = res },
            err => { this.infoService.showErrorMessage('Ihnen ist kein weiterer Flug zugeordnet.') },
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
                    err => { this.infoService.showErrorMessage('Ein unerwarteter Fehler ist aufgetreten.') },
                    () => {
                        this.loaded = true;
                    });
            });
    }

    start(flight: FlightDto) {
        this.isStarted = true;
        this.flightService.updateFlightTracking(flight.id, { action: "Start", delay: 0 } as TrackingDto)
            .subscribe(
                (res) => flight.startTime = res as Date,
                error => this.infoService.showErrorMessage('Ein unerwarteter Fehler ist aufgetreten.')
            );
    }

    land(flight: FlightDto) {
        this.isStarted = false;
        this.flightService.updateFlightTracking(flight.id, { action: "Landung", delay: 0 } as TrackingDto)
            .subscribe(
                res => flight.arrivalTime = res as Date,
                error => this.infoService.showErrorMessage('Ein unerwarteter Fehler ist aufgetreten.')
            );
    }

    delay(flight: FlightDto) {
        this.flightService.updateFlightTracking(flight.id, { action: "Verspätung", delay: this.delayTime } as TrackingDto).subscribe();
        this.delayTransmitted = true;
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
