import {AccountComponent} from './components/account/account.component';
import {AppComponent} from './app.component';
import {appRoutes} from './routes';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {BrowserModule} from '@angular/platform-browser';
import {FlexLayoutModule} from '@angular/flex-layout';
import {FlightOverviewComponent} from './components/flight-overview/flight-overview.component';
import {FlightSearchComponent} from './components/flight-search/flight-search.component';
import {FormsModule} from '@angular/forms';
import {LayoutModule} from '@angular/cdk/layout';
import {MaterialModule} from './material.module';
import {NavigationComponent} from './components/navigation/navigation.component';
import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';


@NgModule({
    declarations: [
        AccountComponent,
        AppComponent,
        FlightOverviewComponent,
        FlightSearchComponent,
        NavigationComponent
    ],
    imports: [
        BrowserAnimationsModule,
        BrowserModule,
        FlexLayoutModule,
        FormsModule,
        LayoutModule,
        MaterialModule,
        RouterModule.forRoot(appRoutes)
    ],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule {
}
