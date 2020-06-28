import {Component, OnInit} from '@angular/core';
import {CheckInTraverDto, FlightDto} from '../../services/dtos/Dtos';
import {CheckInService} from '../../services/checkin/checkin.service';
import {ActivatedRoute} from '@angular/router';
import {InfoService} from '../../services/info/info.service';
import {formatDuration} from 'src/app/services/util/DurationFormatter';

@Component({
    selector: 'app-check-in-page',
    templateUrl: './check-in-page.component.html',
    styleUrls: ['./check-in-page.component.scss']
})
export class CheckInPageComponent implements OnInit {

    passengers: CheckInTraverDto[] = [];
    flight: FlightDto | undefined;

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
                    },
                    error => this.infoService.showErrorMessage('Ihre Daten konnten nicht abgerufen werden')
                );
        });
    }

    get isCheckInComplete() {
        return this.passengers.every(traveler => traveler.checkedIn);
    }

    onDownload(passenger: CheckInTraverDto) {
        this.checkInService.downloadPdf(passenger.id, passenger.name + '_' + passenger.surname);
    }

    getDuration() {
        if (this.flight) {
            return formatDuration(this.flight.arrivalTime.getTime() - this.flight.startTime.getTime());
        }

        return '00:00';
    }

    checkIn(passenger: CheckInTraverDto) {
        if (passenger.checkedIn) {
            this.checkInService.updateCheckIn(passenger.id)
                .subscribe(
                    res => this.infoService.showSuccessMessage('Check-In Erfolgreich'),
                    error => {
                        this.infoService.showErrorMessage('Check-In Fehler');
                        passenger.checkedIn = false;
                    }
                );
        }
    }
}
