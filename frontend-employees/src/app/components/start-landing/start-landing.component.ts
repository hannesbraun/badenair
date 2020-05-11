import {Component, OnInit} from '@angular/core';
import { FlightService } from 'src/app/services/flights/flight.service';
import { FlightDto } from 'src/app/services/dtos/Dtos';

@Component({
    selector: 'app-start-landing',
    templateUrl: './start-landing.component.html',
    styleUrls: ['./start-landing.component.scss']
})
export class StartLandingComponent implements OnInit{
    isStarted = false;
    hasDelay = false;
    name = "Max Mustermann"; //TODO: get Pilot's Name

    dummyFlight!: FlightDto;

    constructor(private flightService: FlightService){}


    ngOnInit(){
        this.dummyFlight = { id: 1, start: "Baden-Baden", destination: "Frankfurt"} as FlightDto;
    }

    start(flight: FlightDto) {
        this.isStarted = true;
        this.flightService.updateFlightTracking(flight.id, "Start").subscribe((res) => flight.startTime = res as Date);
    }

    land(flight: FlightDto) {
        this.isStarted = false;
        this.flightService.updateFlightTracking(flight.id, "Landung").subscribe(res => flight.arrivalTime = res as Date);
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
