import {Component, EventEmitter, Input, Output} from '@angular/core';
import {CheckInTraverDto} from '../../services/dtos/Dtos';

@Component({
    selector: 'app-check-in-info',
    templateUrl: './check-in-info.component.html',
    styleUrls: ['./check-in-info.component.scss']
})
export class CheckInInfoComponent {

    @Input() passenger !: CheckInTraverDto;
    @Output() download = new EventEmitter<void>();
    @Output() onCheckIn = new EventEmitter<CheckInTraverDto>();

    checkIn() {
        this.passenger.checkedIn = true;
        this.onCheckIn.emit(this.passenger);
    }

    onDownload() {
        this.download.emit();
    }

    getBaggageState(state: string): string {
        switch (state) {
            case 'AT_TRAVELLER': return 'Beim Reisenden';
            case 'ON_BAGGAGE_CAROUSEL': return 'Auf dem Gepäckband';
            case 'IN_LUGGAGE_HALL': return 'In der Gepäckhalle';
            case 'ON_LUGGAGE_CART': return 'Auf dem Gepäckwagen';
            case 'ON_PLANE': return 'Im Flugzeug';
            case 'READY_FOR_PICK_UP': return 'Bereit zum Abholen';
        }
        return '';
    }
}
