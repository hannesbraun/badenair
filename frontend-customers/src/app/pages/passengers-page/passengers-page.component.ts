import {Component, OnInit} from '@angular/core';
import {BookingStateService} from '../../services/search/booking-state.service';
import {map} from 'rxjs/operators';
import {Observable} from 'rxjs';
import {Router} from '@angular/router';
import {PassengerDto} from '../../services/dtos/Dtos';

@Component({
    selector: 'app-passengers-page',
    templateUrl: './passengers-page.component.html',
    styleUrls: ['./passengers-page.component.scss']
})
export class PassengersPageComponent implements OnInit {
    passengersCount = new Observable<number>();
    passengers = new Observable<PassengerDto[]>();

    constructor(private bookingStateService: BookingStateService,
                private router: Router,
    ) {
    }

    ngOnInit(): void {
        this.passengersCount = this.bookingStateService.state.pipe(
            map(value => value.passengers)
        );

        this.passengers = this.bookingStateService.state.pipe(
            map(value => value.passengersDto)
        );
    }

    onFromSubmit(value: PassengerDto[]) {
        this.bookingStateService.setPassengersDto(value);
        this.router.navigate(['seat']);
    }
}
