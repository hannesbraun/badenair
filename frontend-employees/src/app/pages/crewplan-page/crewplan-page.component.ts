import {Component, OnInit} from '@angular/core';
import {FlightDto} from '../../services/dtos/Dtos';
import {InfoService} from '../../services/info/info.service';
import { FlightService } from 'src/app/services/flights/flight.service';

@Component({
    selector: 'app-crewplan-page',
    templateUrl: './crewplan-page.component.html'
})
export class CrewplanPageComponent implements OnInit {

    nextFlights: FlightDto[] | undefined;
    displayedColumns = ['start', 'startTime', 'destination', 'arrivalTime'];

    constructor(private flightService: FlightService, private infoService: InfoService) {
    }

    ngOnInit(): void {
        this.flightService.getNextFlights().subscribe(dto => {
            this.nextFlights = dto;
        }, error => this.infoService.showErrorMessage('Die nächsten Flüge konnten nicht abgerufen werden.'));
    }
}
