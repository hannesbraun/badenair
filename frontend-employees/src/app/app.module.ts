import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FlightInfoDialogComponent} from './components/flight-overview/dialogs/flight-info-dialog/flight-info-dialog.component';
import {FlightOverviewComponent} from './components/flight-overview/flight-overview.component';
import {MaterialModule} from './material.module';

@NgModule({
    declarations: [
        AppComponent,
        FlightInfoDialogComponent,
        FlightOverviewComponent
    ],
    imports: [
        BrowserAnimationsModule,
        BrowserModule,
        MaterialModule,
    ],
    providers: [],
    bootstrap: [AppComponent],
    entryComponents: [FlightInfoDialogComponent]
})
export class AppModule {
}
