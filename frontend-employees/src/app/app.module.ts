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
import {AccountComponent} from './components/account/account.component';
import {PlaneMaintenanceComponent} from './components/plane-maintenance/plane-maintenance.component';
import {MatInputModule} from '@angular/material/input';
import {LoginComponent} from './components/login/login.component';

@NgModule({
    declarations: [
        AppComponent,
        FlightInfoDialogComponent,
        FlightOverviewComponent,
        ScheduleConflictDialogComponent,
        NavigationComponent,
        TimeTrackingComponent,
        AccountComponent,
        PlaneMaintenanceComponent,
        LoginComponent
    ],
    imports: [
        BrowserAnimationsModule,
        BrowserModule,
        MaterialModule,
        ReactiveFormsModule,
        AppRoutingModule,
        MatInputModule
    ],
    providers: [],
    bootstrap: [AppComponent],
    entryComponents: [FlightInfoDialogComponent, ScheduleConflictDialogComponent, LoginComponent]
})
export class AppModule {
}
