import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule, Routes} from '@angular/router';
import {FlightSearchComponent} from './components/flight-search/flight-search.component';
import {SignUpPageComponent} from './pages/sign-up-page/sign-up-page.component';
import {FlightsPageComponent} from './pages/flights-page/flights-page.component';
import {AccountSettingsPageComponent} from './pages/account-settings-page/account-settings-page.component';
import {BookedFlightsComponent} from './components/booked-flights/booked-flights.component';
import {PassengersFormComponent} from './components/passengers-form/passengers-form.component';
import {CheckInPageComponent} from './pages/check-in-page/check-in-page.component';
import { BookingOverviewComponent } from './components/booking-overview/booking-overview.component';


export const appRoutes: Routes = [
    {path: '', component: FlightSearchComponent},
    {path: 'settings', component: AccountSettingsPageComponent},
    {path: 'flights', component: FlightsPageComponent},
    {path: 'signup', component: SignUpPageComponent},
    {path: 'booked', component: BookedFlightsComponent},
    {path: 'passengers', component: PassengersFormComponent},
    {path: 'checkin', component: CheckInPageComponent},
    {path: 'overview', component: BookingOverviewComponent},
];

@NgModule({
    declarations: [],
    imports: [
        CommonModule,
        RouterModule.forRoot(appRoutes)
    ],
    exports: [
        RouterModule
    ]
})
export class AppRoutingModule {
}
