import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {FlightOverviewComponent} from './components/flight-overview/flight-overview.component';
import {TimeTrackingComponent} from './components/time-tracking/time-tracking.component';
import {PlaneMaintenanceComponent} from './components/plane-maintenance/plane-maintenance.component';
import {UpdateBaggagePageComponent} from './pages/update-baggage-page/update-baggage-page.component';
import {ServiceSchedulePageComponent} from './pages/service-schedule-page/service-schedule-page.component';
import {StartLandingComponent} from './components/start-landing/start-landing.component';
import {VacationPlanningComponent} from './components/vacation-planning/vacation-planning.component';
import {ShiftSchedulePageComponent} from './pages/shift-schedule-page/shift-schedule-page.component';
import {AuthGuard} from './services/auth/auth.guard';


const routes: Routes = [
    {path: 'flightoverview', component: FlightOverviewComponent, canActivate: [AuthGuard]},
    {path: 'maintenance', component: PlaneMaintenanceComponent, canActivate: [AuthGuard]},
    {path: 'time', component: TimeTrackingComponent, canActivate: [AuthGuard]},
    {path: 'baggage', component: UpdateBaggagePageComponent, canActivate: [AuthGuard]},
    {path: 'serviceSchedule', component: ServiceSchedulePageComponent, canActivate: [AuthGuard]},
    {path: 'start-landing', component: StartLandingComponent, canActivate: [AuthGuard]},
    {path: 'vacation-planning', component: VacationPlanningComponent, canActivate: [AuthGuard]},
    {path: 'shiftSchedule', component: ShiftSchedulePageComponent, canActivate: [AuthGuard]},
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})

export class AppRoutingModule {
}
