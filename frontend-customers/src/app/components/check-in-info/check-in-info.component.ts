import {Component, EventEmitter, Input, Output} from '@angular/core';
import {TravelerDto} from '../../services/dtos/Dtos';

@Component({
    selector: 'app-check-in-info',
    templateUrl: './check-in-info.component.html',
    styleUrls: ['./check-in-info.component.scss']
})
export class CheckInInfoComponent {

    @Input() passenger !: TravelerDto;
    @Output() download = new EventEmitter<void>();
    @Input() isCheckInComplete !: boolean;

    constructor() {
    }

    get isPassengerSelected(): boolean {
        return this.passenger.checkedIn;
    }

    onDownload() {
        this.download.emit();
    }
}
