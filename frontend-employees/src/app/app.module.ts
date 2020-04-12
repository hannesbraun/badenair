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
import {PlaneMaintenanceComponent} from './components/plane-maintenance/plane-maintenance.component';
import {UpdateBaggageComponent} from './components/update-baggage/update-baggage.component';
import {ServiceSchedulePageComponent} from './pages/service-schedule-page/service-schedule-page.component';
import {UpdateBaggagePageComponent} from './pages/update-baggage-page/update-baggage-page.component';
import {StartLandingComponent} from './components/start-landing/start-landing.component';
import {VacationPlanningComponent} from './components/vacation-planning/vacation-planning.component';
import {ShiftSchedulePageComponent} from './pages/shift-schedule-page/shift-schedule-page.component';
import {AuthModule} from './auth/auth.module';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {OAuthModule} from 'angular-oauth2-oidc';
import {environment} from '../environments/environment';
import {LandingPageComponent} from './pages/landing-page/landing-page.component';
import {VacationPlanningPageComponent} from './pages/vacation-planning-page/vacation-planning-page.component';
import {DateInterceptor} from './services/util/date.interceptor';

@NgModule({
    declarations: [
        AppComponent,
        FlightInfoDialogComponent,
        FlightOverviewComponent,
        ScheduleConflictDialogComponent,
        NavigationComponent,
        TimeTrackingComponent,
        PlaneMaintenanceComponent,
        UpdateBaggageComponent,
        ServiceSchedulePageComponent,
        UpdateBaggagePageComponent,
        StartLandingComponent,
        VacationPlanningComponent,
        ShiftSchedulePageComponent,
        LandingPageComponent,
        VacationPlanningPageComponent
    ],
    imports: [
        BrowserAnimationsModule,
        BrowserModule,
        MaterialModule,
        ReactiveFormsModule,
        AppRoutingModule,
        AuthModule,
        HttpClientModule,
        OAuthModule.forRoot({
            resourceServer: {
                allowedUrls: [environment.backendApiRoot],
                sendAccessToken: true
            }
        })
    ],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: DateInterceptor,
            multi: true
        }
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
