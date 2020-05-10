import {Component, OnInit} from '@angular/core';
import { BookingStateService } from 'src/app/services/search/booking-state.service';
import { FlightDto } from 'src/app/services/dtos/Dtos';
import { Subscription } from 'rxjs';



@Component({
    selector: 'app-success-page',
    templateUrl: './success-page.component.html',
    styleUrls: ['./success-page.component.scss']
})
export class SuccessPageComponent implements OnInit{
    
    private bookingStateSubscription !: Subscription;
    flights!: FlightDto[];

    constructor(private bookingStateService: BookingStateService) {}
    
    ngOnInit(){
        this.flights = [];
        this.bookingStateSubscription = this.bookingStateService.state
        .subscribe(state => {
            if(state.selectedToFlight){
                this.flights.push(state.selectedToFlight);
            }

            if (state.selectedReturnFlight){
                this.flights.push(state.selectedReturnFlight);
            }
        });
    }

    getDuration(flight: FlightDto): number {
        return flight.arrivalTime.getTime() - flight.startTime.getTime();
    } 

    ngOnDestroy(): void {
        this.bookingStateSubscription.unsubscribe();
    }
}
