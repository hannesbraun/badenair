import {Component, OnInit} from '@angular/core';
import {MaintenanceService} from 'src/app/services/maintenance/maintenance.service';
import {PlaneMaintenance} from 'src/app/services/dtos/Dtos';
import {InfoService} from '../../services/info/info.service';

@Component({
    selector: 'app-plane-maintenance',
    templateUrl: './plane-maintenance.component.html',
    styleUrls: ['./plane-maintenance.component.scss']
})
export class PlaneMaintenanceComponent implements OnInit {
    planes: PlaneMaintenance[] = [];

    constructor(private maintenanceService: MaintenanceService, private infoService: InfoService) {

    }

    ngOnInit() {
        this.getPlanes();
    }

    getPlanes() {
        this.maintenanceService.getMaintenanceList()
            .subscribe(
                result => this.planes = result,
                error => this.infoService.showErrorMessage('Ein unerwarteter Fehler ist aufgetreten')
            );
    }

    getIndicatorLength(distance: number) {
        return {transform: `scaleX(${distance / 1000 % 1})`};
    }

    onRepairButtonPressed(currentPlane: PlaneMaintenance) {
        this.maintenanceService.updateMaintenance(currentPlane.id)
            .subscribe(
                () => null,
                error => this.infoService.showErrorMessage('Ein unerwarteter Fehler ist aufgetreten')
            );
        currentPlane.state = 'WAITING';
    }
}
