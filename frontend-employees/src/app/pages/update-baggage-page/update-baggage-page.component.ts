import {Component} from '@angular/core';
import {BaggageService} from '../../services/baggage/baggage.service';
import {ChangeBaggageStateDto} from '../../services/dtos/Dtos';
import {InfoService} from '../../services/info/info.service';

@Component({
    selector: 'app-update-baggage-page',
    template: `
        <h1>Gepäckstatus aktualisieren</h1>
        <app-update-baggage (updateBaggageState)="onUpdateBaggageState($event)"></app-update-baggage>
    `,
    styles: []
})
export class UpdateBaggagePageComponent {

    constructor(private baggageService: BaggageService, private infoService: InfoService) {
    }

    onUpdateBaggageState(dto: ChangeBaggageStateDto) {
        this.baggageService.updateBaggageState(dto)
            .subscribe(
                () => null,
                error => this.infoService.showErrorMessage('Der Gepächstatus konnte nicht abgerufen werden')
            );
    }
}
