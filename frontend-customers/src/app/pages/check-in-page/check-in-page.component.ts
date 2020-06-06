import {Component, OnInit} from '@angular/core';
import {FlightDto, TravelerDto} from '../../services/dtos/Dtos';
import {CheckInService} from '../../services/checkin/checkin.service';
import {ActivatedRoute} from '@angular/router';
import {InfoService} from '../../services/info/info.service';
import { formatDuration } from 'src/app/services/util/DurationFormatter';

@Component({
    selector: 'app-check-in-page',
    templateUrl: './check-in-page.component.html',
    styleUrls: ['./check-in-page.component.scss']
})
export class CheckInPageComponent implements OnInit {

    passengers: TravelerDto[] = [];
    flight: FlightDto | undefined;
    isCheckInComplete = false;

    constructor(
        private checkInService: CheckInService,
        private route: ActivatedRoute,
        private infoService: InfoService,
    ) {
    }

    ngOnInit(): void {
        this.route.paramMap.subscribe(params => {
            const id = parseInt(params.get('id') ?? '0', 10);

            if (id === 0) {
                return;
            }

            this.checkInService.getCheckInInfo(id)
                .subscribe(dto => {
                        this.passengers = dto.travelers;
                        this.flight = dto.flight;
                        this.isCheckInComplete = this.passengers.every(traveler => traveler.checkedIn);
                    },
                    error => this.infoService.showErrorMessage('Ihre Daten konnten nicht abgerufen werden')
                );
        });
    }

    onDownload(passenger: TravelerDto) {
        this.checkInService.downloadPdf(passenger.id, passenger.name + '_' + passenger.surname);
    }

    getDuration() {
        if (this.flight) {
            return formatDuration(this.flight.arrivalTime.getTime() - this.flight.startTime.getTime());
        }

        return "00:00";
    }

    checkIn() {
        this.passengers.forEach(passenger => {
            if (passenger.checkedIn) {
                this.checkInService.updateCheckIn(passenger.id)
                    .subscribe(
                        res => null,
                        error => this.infoService.showErrorMessage('Check-In Fehler')
                    );
            }
        });
        this.isCheckInComplete = true;
    }
}
