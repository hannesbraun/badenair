import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FlightInfoDialogComponent} from './components/flight-overview/dialogs/flight-info-dialog/flight-info-dialog.component';
import {FlightOverviewComponent} from './components/flight-overview/flight-overview.component';
import {MaterialModule} from './material.module';
import {ScheduleConflictDialogComponent} from './components/flight-overview/dialogs/schedule-conflict-dialog/schedule-conflict-dialog.component';
import {ReactiveFormsModule} from '@angular/forms';

import {NavigationComponent} from './components/navigation/navigation.component';
import {TimeTrackingComponent} from './components/time-tracking/time-tracking.component';
import {AppRoutingModule} from './app-routing.module';
import {LoginComponent} from './components/login/login.component';
import {PlaneMaintenanceComponent} from './components/plane-maintenance/plane-maintenance.component';
import {UpdateBaggageComponent} from './components/update-baggage/update-baggage.component';
import {ServiceSchedulePageComponent} from './pages/service-schedule-page/service-schedule-page.component';
import {UpdateBaggagePageComponent} from './pages/update-baggage-page/update-baggage-page.component';
import {StartLandingComponent} from './components/start-landing/start-landing.component';
import {VacationPlanningComponent} from './components/vacation-planning/vacation-planning.component';
import {ShiftSchedulePageComponent} from './pages/shift-schedule-page/shift-schedule-page.component';

@NgModule({
    declarations: [
        AppComponent,
        FlightInfoDialogComponent,
        FlightOverviewComponent,
        ScheduleConflictDialogComponent,
        NavigationComponent,
        TimeTrackingComponent,
        PlaneMaintenanceComponent,
        LoginComponent,
        UpdateBaggageComponent,
        ServiceSchedulePageComponent,
        UpdateBaggagePageComponent,
        StartLandingComponent,
        VacationPlanningComponent,
        ShiftSchedulePageComponent
    ],
    imports: [
        BrowserAnimationsModule,
        BrowserModule,
        MaterialModule,
        ReactiveFormsModule,
        AppRoutingModule
    ],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule {
}
