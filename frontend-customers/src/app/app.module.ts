import {AccountSettingsPageComponent} from './pages/account-settings-page/account-settings-page.component';
import {AppComponent} from './app.component';
import {AppRoutingModule} from './app-routing.module';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {BrowserModule} from '@angular/platform-browser';
import {FlexLayoutModule} from '@angular/flex-layout';
import {FlightSearchComponent} from './components/flight-search/flight-search.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {LayoutModule} from '@angular/cdk/layout';
import {MaterialModule} from './material.module';
import {NavigationComponent} from './components/navigation/navigation.component';
import {NgModule} from '@angular/core';
import {ProfileComponent} from './components/profile/profile.component';
import {ProfileFormComponent} from './components/profile-form/profile-form.component';
import {SignUpComponent} from './components/signup/sign-up.component';
import {SignUpPageComponent} from './pages/sign-up-page/sign-up-page.component';
import {FlightComponent} from './components/flight/flight.component';
import {FlightInfoComponent} from './components/flight/flight-info/flight-info.component';
import {CheckButtonComponent} from './components/flight/check-button/check-button.component';
import {FlightsPageComponent} from './pages/flights-page/flights-page.component';
import {HoverClassDirective} from './directives/hover-class.directive';
import {BookedFlightsComponent} from './components/booked-flights/booked-flights.component';
import {PassengersFormComponent} from './components/passengers-form/passengers-form.component';
import {CheckInInfoComponent} from './components/check-in-info/check-in-info.component';
import {CheckInPageComponent} from './pages/check-in-page/check-in-page.component';
import {SuccessPageComponent} from './pages/success-page/success-page.component';
import {BookingOverviewComponent} from './components/booking-overview/booking-overview.component';
import {AuthModule} from './auth/auth.module';
import {OAuthModule} from 'angular-oauth2-oidc';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {environment} from '../environments/environment';
import {SignupDialogComponent} from './components/signup-dialog/signup-dialog.component';
import {DateInterceptor} from './services/util/date.interceptor';
import {FlightSearchPageComponent} from './pages/flight-search-page/flight-search-page.component';
import { PassengersPageComponent } from './pages/passengers-page/passengers-page.component';
import { SeatSelectionPageComponent } from './pages/seat-selection-page/seat-selection-page.component';
import { SeatSelectionComponent } from './components/seat-selection/seat-selection.component';
import {BookingOverviewPageComponent} from './pages/booking-overview-page/booking-overview-page.component';

@NgModule({
    declarations: [
        AccountSettingsPageComponent,
        AppComponent,
        FlightSearchComponent,
        NavigationComponent,
        ProfileComponent,
        ProfileFormComponent,
        SignUpComponent,
        SignUpPageComponent,
        FlightComponent,
        FlightInfoComponent,
        CheckButtonComponent,
        FlightsPageComponent,
        HoverClassDirective,
        BookedFlightsComponent,
        PassengersFormComponent,
        BookedFlightsComponent,
        CheckInInfoComponent,
        CheckInPageComponent,
        SuccessPageComponent,
        BookingOverviewComponent,
        SignupDialogComponent,
        FlightSearchPageComponent,
        PassengersPageComponent,
        SeatSelectionPageComponent,
        SeatSelectionComponent,
        BookingOverviewPageComponent
    ],
    imports: [
        AppRoutingModule,
        BrowserAnimationsModule,
        BrowserModule,
        FlexLayoutModule,
        FormsModule,
        LayoutModule,
        MaterialModule,
        ReactiveFormsModule,
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
