import {Component} from '@angular/core';

export interface Flight {
    start: string;
    destination: string;
    departureDate: Date;
    arrivalDate: Date;
}

@Component({
    selector: 'app-success-page',
    templateUrl: './success-page.component.html',
    styleUrls: ['./success-page.component.scss']
})
export class SuccessPageComponent {

    flights: Flight[] | undefined;

    constructor() {
        this.flights = [{
            start: "Basel",
            destination: "Porto",
            departureDate: new Date(),
            arrivalDate: new Date()
        }, {
            start: "Porto",
            destination: "Basel",
            departureDate: new Date(),
            arrivalDate: new Date()
        }]
    }

    getDuration(flight: Flight): number {
        return flight.arrivalDate.getTime() - flight.departureDate.getTime();
    } 
}
