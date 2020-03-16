import { Component, OnInit } from '@angular/core';
import { FlightService } from 'src/app/services/flight/flight.service';
import { FlightDto, PassengerDto } from 'src/app/services/dtos/Dtos';
import { PassengerService } from 'src/app/services/passenger/passenger.service';

@Component({
  selector: 'app-booking-overview',
  templateUrl: './booking-overview.component.html',
  styleUrls: ['./booking-overview.component.scss']
})
export class BookingOverviewComponent implements OnInit {
  flight !: FlightDto;
  passengers!: PassengerDto[]
  constructor(private flightService: FlightService, private passengerService: PassengerService) { }

  ngOnInit(): void {
    this.flightService.getFlight(1).subscribe(dto => {
      this.flight = dto;
    });
    this.passengerService.getPassengersForFlight(1).subscribe(
      dto => {this.passengers = dto;}
    );
  }

  getDuration() {
    if (this.flight) {
        return this.flight.arrivalTime.getTime() - this.flight.startTime.getTime();
    }

    return 0;
}

}
