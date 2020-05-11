import {Component, OnDestroy, OnInit} from '@angular/core';
import {BookingStateService} from '../../services/search/booking-state.service';
import {Subscription} from 'rxjs';

@Component({
    selector: 'app-booking-overview-page',
    templateUrl: './booking-overview-page.component.html',
    styleUrls: ['./booking-overview-page.component.scss']
})
export class BookingOverviewPageComponent implements OnInit, OnDestroy {
    private bookingStateSubscription!: Subscription;

    constructor(private bookingStateService: BookingStateService) {
    }

    ngOnInit(): void {
        // TODO: fill Overview with State Data
        this.bookingStateSubscription = this.bookingStateService.state
            .subscribe(state => console.log);
    }

    ngOnDestroy(): void {
        this.bookingStateSubscription.unsubscribe();
    }
}
