import {Component, Input} from '@angular/core';
import {FlightDto} from '../../services/dtos/Dtos';

@Component({
    selector: 'app-flight',
    templateUrl: './flight.component.html',
    styleUrls: ['./flight.component.scss']
})
export class FlightComponent {

    @Input() numberOfPassengers !: number;
    @Input() price !: number;
    @Input() flight !: FlightDto;

}
