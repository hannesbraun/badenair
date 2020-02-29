import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {FlightOverviewComponent} from './components/flight-overview/flight-overview.component';
import {AccountComponent} from './components/account/account.component';
import {TimeTrackingComponent} from './components/time-tracking/time-tracking.component';
import {PlaneMaintenanceComponent} from './components/plane-maintenance/plane-maintenance.component';


const routes: Routes = [
    {path: 'flightoverview', component: FlightOverviewComponent},
    {path: 'account', component: AccountComponent},
    {path: 'time', component: TimeTrackingComponent},
    {path: 'maintenance', component: PlaneMaintenanceComponent}
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})

export class AppRoutingModule {
}
