import {AccountComponent} from './components/account/account.component';
import {AppComponent} from './app.component';
import {AppRoutingModule} from './app-routing.module';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {BrowserModule} from '@angular/platform-browser';
import {FlexLayoutModule} from '@angular/flex-layout';
import {FlightOverviewComponent} from './components/flight-overview/flight-overview.component';
import {FlightSearchComponent} from './components/flight-search/flight-search.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {LayoutModule} from '@angular/cdk/layout';
import {MaterialModule} from './material.module';
import {NavigationComponent} from './components/navigation/navigation.component';
import {NgModule} from '@angular/core';
import {SignUpComponent} from './components/signup/sign-up.component';
import {SignUpPageComponent} from './pages/sign-up-page/sign-up-page.component';
import {FlightComponent} from './components/flight/flight.component';
import {FlightInfoComponent} from './components/flight/flight-info/flight-info.component';
import {CheckButtonComponent} from './components/flight/check-button/check-button.component';
import {FlightsPageComponent} from './pages/flights-page/flights-page.component';
import {HoverClassDirective} from './directives/hover-class.directive';


@NgModule({
    declarations: [
        AccountComponent,
        AppComponent,
        FlightOverviewComponent,
        FlightSearchComponent,
        NavigationComponent,
        SignUpComponent,
        SignUpPageComponent,
        FlightComponent,
        FlightInfoComponent,
        CheckButtonComponent,
        FlightsPageComponent,
        HoverClassDirective
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
    bootstrap: [AppComponent]
})
export class AppModule {
}
