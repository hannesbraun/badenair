import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {FlightOverviewComponent} from './components/flight-overview/flight-overview.component';
import {TimeTrackingComponent} from './components/time-tracking/time-tracking.component';
import {PlaneMaintenanceComponent} from './components/plane-maintenance/plane-maintenance.component';
import {UpdateBaggagePageComponent} from './pages/update-baggage-page/update-baggage-page.component';
import {ServiceSchedulePageComponent} from './pages/service-schedule-page/service-schedule-page.component';
import {StartLandingComponent} from './components/start-landing/start-landing.component';
import {VacationPlanningComponent} from './components/vacation-planning/vacation-planning.component';


const routes: Routes = [
    {path: 'flightoverview', component: FlightOverviewComponent},
    {path: 'maintenance', component: PlaneMaintenanceComponent},
    {path: 'time', component: TimeTrackingComponent},
    {path: 'baggage', component: UpdateBaggagePageComponent},
    {path: 'serviceSchedule', component: ServiceSchedulePageComponent},
    {path: 'start-landing', component: StartLandingComponent},
    {path: 'vacation-planning', component: VacationPlanningComponent},
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})

export class AppRoutingModule {
}
