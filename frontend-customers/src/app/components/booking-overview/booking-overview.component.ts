import { Component, Input } from '@angular/core';

import { FlightDto, PassengerDto } from 'src/app/services/dtos/Dtos';
import { Seat } from '../seat-selection/seat-selection.component';

@Component({
  selector: 'app-booking-overview',
  templateUrl: './booking-overview.component.html',
  styleUrls: ['./booking-overview.component.scss']
})
export class BookingOverviewComponent {
  @Input() toFlight !: FlightDto;
  @Input() returnFlight !: FlightDto;
  @Input() passengers!: PassengerDto[];
  @Input() toSeats !: Seat[];
  @Input() returnSeats !: Seat[];
  @Input() price!: number;

  getDuration(flight: FlightDto) {
    if (flight) {
      return flight.arrivalTime.getTime() - flight.startTime.getTime();
    }

    return 0;
  }
  
  convertToSeatNumber(column: number): string{
    return String.fromCharCode(65+column);
  }

}
