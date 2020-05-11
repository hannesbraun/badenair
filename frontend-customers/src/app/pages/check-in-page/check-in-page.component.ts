import {Component, OnInit} from '@angular/core';
import {PassengerService} from '../../services/passenger/passenger.service';
import {FlightDto, PassengerDto} from '../../services/dtos/Dtos';
import {FlightService} from '../../services/flight/flight.service';
import {CheckInService} from '../../services/checkin/checkin.service';

@Component({
    selector: 'app-check-in-page',
    templateUrl: './check-in-page.component.html',
    styleUrls: ['./check-in-page.component.scss']
})
export class CheckInPageComponent implements OnInit {

    passengers!: PassengerDto[];
    flight: FlightDto | undefined;
    isCheckInComplete: boolean = false;

    constructor(private passengerService: PassengerService, private flightService: FlightService, private checkInService: CheckInService) {
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
        this.passengers.forEach(passenger => {
            if(passenger.checkedIn){
                this.checkInService.updateCheckIn(passenger.id).subscribe(result=> console.log);
                // Seatnumber is still const
            }
        });
        this.isCheckInComplete = true;
    }
}
