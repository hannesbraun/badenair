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
import { SignUpComponent } from './components/signup/sign-up.component';


@NgModule({
    declarations: [
        AccountComponent,
        AppComponent,
        FlightOverviewComponent,
        FlightSearchComponent,
        NavigationComponent,
        SignUpComponent
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
