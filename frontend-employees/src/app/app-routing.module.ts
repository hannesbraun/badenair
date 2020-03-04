import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {FlightOverviewComponent} from './components/flight-overview/flight-overview.component';
import {TimeTrackingComponent} from './components/time-tracking/time-tracking.component';
import {PlaneMaintenanceComponent} from './components/plane-maintenance/plane-maintenance.component';
import {UpdateBaggagePageComponent} from './pages/update-baggage-page/update-baggage-page.component';
import {ServiceSchedulePageComponent} from './pages/service-schedule-page/service-schedule-page.component';


const routes: Routes = [
    {path: 'flightoverview', component: FlightOverviewComponent},
    {path: 'maintenance', component: PlaneMaintenanceComponent},
    {path: 'time', component: TimeTrackingComponent},
    {path: 'baggage', component: UpdateBaggagePageComponent},
    {path: 'time', component: TimeTrackingComponent},
    {path: 'serviceSchedule', component: ServiceSchedulePageComponent}
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})

export class AppRoutingModule {
}
