import {Component, OnInit} from '@angular/core';

@Component({
    selector: 'app-plane-maintenance',
    templateUrl: './plane-maintenance.component.html',
    styleUrls: ['./plane-maintenance.component.scss']
})
export class PlaneMaintenanceComponent implements OnInit {
    planes = [
        {name: 'Flugzeug 1', distance: 1329, isDefect: false},
        {name: 'Flugzeug 2', distance: 5959, isDefect: true},
        {name: 'Flugzeug 3', distance: 13155, isDefect: true},
        {name: 'Flugzeug 4', distance: 1642, isDefect: false}
    ];

    constructor() {
    }

    ngOnInit() {
    }

    getIndicatorLength(distance: number) {
        return {transform: `scaleX(${distance / 1000 % 1})`};
    }
}
