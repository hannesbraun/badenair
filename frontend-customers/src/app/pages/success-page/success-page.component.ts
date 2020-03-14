import {Component, OnInit} from '@angular/core';

@Component({
    selector: 'app-success-page',
    templateUrl: './success-page.component.html',
    styleUrls: ['./success-page.component.scss']
})
export class SuccessPageComponent implements OnInit {

    start: string | undefined;
    destination: string | undefined;
    departureDate1: Date | undefined;
    arrivalDate1: Date | undefined;
    departureDate2: Date | undefined;
    arrivalDate2: Date | undefined;

    constructor() {
    }

    ngOnInit(): void {
        this.start = "Basel";
        this.destination = "Porto";

        this.departureDate1 = new Date();
        this.arrivalDate1 = new Date();
        this.departureDate2 = new Date();
        this.arrivalDate2 = new Date();
    }

    getDuration(id: number): number {
        if (id === 1 && this.departureDate1 && this.arrivalDate1) {
            return this.arrivalDate1.getTime() - this.departureDate1.getTime();
        } else if (id === 2 && this.departureDate2 && this.arrivalDate2) {
            return this.arrivalDate2.getTime() - this.departureDate2.getTime();
        } else {
            return 0;
        }
    } 
}
