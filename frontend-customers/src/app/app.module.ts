import {AccountSettingsPageComponent} from './pages/account-settings-page/account-settings-page.component';
import {AppComponent} from './app.component';
import {AppRoutingModule} from './app-routing.module';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {BrowserModule} from '@angular/platform-browser';
import {ChangePasswordDialogComponent} from './components/profile/change-password-dialog/change-password-dialog.component';
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
import {LoginComponent} from './components/login/login.component';


@NgModule({
    declarations: [
        AccountSettingsPageComponent,
        AppComponent,
        ChangePasswordDialogComponent,
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
        LoginComponent
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
    ],
    providers: [],
    bootstrap: [AppComponent],
    entryComponents: [ChangePasswordDialogComponent, LoginComponent]
})
export class AppModule {
}
