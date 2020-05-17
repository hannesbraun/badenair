import {Component, OnInit} from '@angular/core';
import {FlightDto, TravelerDto} from '../../services/dtos/Dtos';
import {CheckInService} from '../../services/checkin/checkin.service';
import {ActivatedRoute} from '@angular/router';

@Component({
    selector: 'app-check-in-page',
    templateUrl: './check-in-page.component.html',
    styleUrls: ['./check-in-page.component.scss']
})
export class CheckInPageComponent implements OnInit {

    passengers: TravelerDto[] = [];
    flight: FlightDto | undefined;
    isCheckInComplete = false;

    constructor(private checkInService: CheckInService, private route: ActivatedRoute) {
    }

    ngOnInit(): void {
        this.route.paramMap.subscribe(params => {
            const id = parseInt(params.get('id') ?? '0', 10);

            if (id === 0) {
                return;
            }

            this.checkInService.getCheckInInfo(id).subscribe(dto => {
                this.passengers = dto.travelers;
                this.flight = dto.flight;
                this.isCheckInComplete = this.passengers.every(traveler => traveler.checkedIn);
            });
        });
    }

    onDownload(passenger: TravelerDto) {
        // TODO: handle download
    }

    getDuration() {
        if (this.flight) {
            return this.flight.arrivalTime.getTime() - this.flight.startTime.getTime();
        }

        return 0;
    }

    checkIn() {
        this.passengers.forEach(passenger => {
            if (passenger.checkedIn) {
                // TODO: add right Seat Number
                this.checkInService.updateCheckIn(passenger.id, 1).subscribe();
            }
        });
        this.isCheckInComplete = true;
    }
}
