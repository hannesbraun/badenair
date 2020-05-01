import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';

@Component({
    selector: 'app-seat-selection-page',
    templateUrl: './seat-selection-page.component.html',
    styleUrls: ['./seat-selection-page.component.scss']
})
export class SeatSelectionPageComponent implements OnInit {

    constructor(private route: Router) {
    }

    ngOnInit(): void {
    }

    seatSelected() {
        this.route.navigate(['overview']);
    }
}
