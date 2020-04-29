import {Component, OnInit} from '@angular/core';
import {BookingStateService} from '../../services/search/booking-state.service';
import {map} from 'rxjs/operators';
import {Observable} from 'rxjs';

@Component({
    selector: 'app-passengers-page',
    templateUrl: './passengers-page.component.html',
    styleUrls: ['./passengers-page.component.scss']
})
export class PassengersPageComponent implements OnInit {
    passengers = new Observable<number>();

    constructor(private bookingStateService: BookingStateService,
    ) {
    }

    ngOnInit(): void {
        this.passengers = this.bookingStateService.state.pipe(
            map(value => value.passengers)
        );
    }
}
