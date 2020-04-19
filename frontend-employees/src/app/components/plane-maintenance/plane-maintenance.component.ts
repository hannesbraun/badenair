import {Component, OnInit} from '@angular/core';
import { MaintenanceService } from 'src/app/services/maintenance/maintenance.service';
import { PlaneMaintenance } from 'src/app/services/dtos/Dtos';

@Component({
    selector: 'app-plane-maintenance',
    templateUrl: './plane-maintenance.component.html',
    styleUrls: ['./plane-maintenance.component.scss']
})
export class PlaneMaintenanceComponent implements OnInit {
    planes: PlaneMaintenance[] = [];

    constructor(private maintenanceService : MaintenanceService) {

    }

    ngOnInit() {
        this.getPlanes();
    }

    getPlanes(){
        this.maintenanceService.getMaintenanceList().subscribe(result=>this.planes = result);
    }

    getIndicatorLength(distance: number) {
        return {transform: `scaleX(${distance / 1000 % 1})`};
    }

    onRepairButtonPressed(currentPlane: PlaneMaintenance){
        this.maintenanceService.updateMaintenance(currentPlane.id);
    }
}
