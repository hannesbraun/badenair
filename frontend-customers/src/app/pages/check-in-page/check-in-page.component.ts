import {Component, OnInit} from '@angular/core';
import {PassengerService} from '../../services/passenger/passenger.service';
import {FlightDto, PassengerDto} from '../../services/dtos/Dtos';
import {FlightService} from '../../services/flight/flight.service';

@Component({
    selector: 'app-check-in-page',
    templateUrl: './check-in-page.component.html',
    styleUrls: ['./check-in-page.component.scss']
})
export class CheckInPageComponent implements OnInit {

    passengers: PassengerDto[] | undefined;
    flight: FlightDto | undefined;
    isCheckInComplete: boolean = false;

    constructor(private passengerService: PassengerService, private flightService: FlightService) {
    }

    ngOnInit(): void {
        this.passengerService.getPassengersForFlight(1).subscribe(dtos => {
            this.passengers = dtos;
        });

        this.flightService.getFlight(1).subscribe(dto => {
            this.flight = dto;
        });
    }

    onDownload(passenger: PassengerDto) {
        // TODO: handle download
    }

    getDuration() {
        if (this.flight) {
            return this.flight.arrivalTime.getTime() - this.flight.startTime.getTime();
        }

        return 0;
    }

    checkIn() {
        this.isCheckInComplete = true;
    }
}
