import {Component, OnInit} from '@angular/core';
import {BaggageService} from '../../services/baggage/baggage.service';
import {FlightService} from '../../services/flights/flight.service';
import {ChangeBaggageStateDto, FlightDto} from '../../services/dtos/Dtos';
import {map} from 'rxjs/operators';

@Component({
    selector: 'app-update-baggage-page',
    template: `
        <h1>Gepäckstatus aktualisieren</h1>
        <app-update-baggage [availableFlights]="availableFlights"
                            (updateBaggageState)="onUpdateBaggageState($event)"></app-update-baggage>
    `,
    styles: []
})
export class UpdateBaggagePageComponent implements OnInit {

    availableFlights: FlightDto[] = [];

    constructor(private baggageService: BaggageService, private flightService: FlightService) {
    }

    ngOnInit(): void {
        // TODO: Replace
        this.flightService.getPlaneSchedules().pipe(
            map(schedules => schedules[0]),
            map(schedule => schedule.flights)
        ).subscribe(flights => this.availableFlights = flights);
    }

    onUpdateBaggageState(dto: ChangeBaggageStateDto) {
        this.baggageService.updateBaggageState(dto).subscribe();
    }
}
