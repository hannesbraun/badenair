import { Component, OnInit, Input } from '@angular/core';

import { FlightDto, PassengerDto } from 'src/app/services/dtos/Dtos';
import { Seat } from '../seat-selection/seat-selection.component';

@Component({
  selector: 'app-booking-overview',
  templateUrl: './booking-overview.component.html',
  styleUrls: ['./booking-overview.component.scss']
})
export class BookingOverviewComponent implements OnInit {
  @Input() toFlight !: FlightDto;
  @Input() returnFlight !: FlightDto;
  @Input() passengers!: PassengerDto[];
  @Input() toSeats !: Seat[];
  @Input() returnSeats !: Seat[];
  constructor() { }

  ngOnInit(): void {
    console.log(this.toSeats);
  }

  getDuration(flight: FlightDto) {
    if (flight) {
      return flight.arrivalTime.getTime() - flight.startTime.getTime();
    }

    return 0;
  }

  calculatePrice(){
    let price = 0;
    if(this.toFlight && this.returnFlight){
      price += this.toFlight.price + this.returnFlight.price;
    }
    else if (this.toFlight){
      price += this.toFlight.price;
    }
    this.passengers.forEach(passenger => {
      price += passenger.baggage1 + passenger.baggage2 + passenger.baggage3 + passenger.baggage4;
    })

    return price;
  }
  

}
