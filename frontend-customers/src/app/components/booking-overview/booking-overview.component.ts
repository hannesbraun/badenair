import { Component, Input } from '@angular/core';

import { FlightDto, TravelerDto } from 'src/app/services/dtos/Dtos';
import { Seat } from '../seat-selection/seat-selection.component';
import { formatDuration } from '../..//services/util/DurationFormatter';

@Component({
  selector: 'app-booking-overview',
  templateUrl: './booking-overview.component.html',
  styleUrls: ['./booking-overview.component.scss']
})
export class BookingOverviewComponent {
  @Input() toFlight !: FlightDto;
  @Input() returnFlight !: FlightDto;
  @Input() passengers!: TravelerDto[];
  @Input() toSeats !: Seat[];
  @Input() returnSeats !: Seat[];
  @Input() price!: number;
  @Input() baggagePrice!: number;

  getDuration(flight: FlightDto) {
    if (flight) {
      return formatDuration(flight.arrivalTime.getTime() - flight.startTime.getTime());
    }

    return "00:00";
  }

  convertToSeatNumber(column: number): string {
    return String.fromCharCode(65 + column);
  }

}
