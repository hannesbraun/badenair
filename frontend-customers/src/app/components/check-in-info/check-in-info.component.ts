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
    @Output() onCheckIn = new EventEmitter<TravelerDto>();

    checkIn() {
        this.passenger.checkedIn = true;
        this.onCheckIn.emit(this.passenger);
    }

    onDownload() {
        this.download.emit();
    }
}
