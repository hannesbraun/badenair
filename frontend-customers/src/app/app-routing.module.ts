import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule, Routes} from '@angular/router';
import {SignUpPageComponent} from './pages/sign-up-page/sign-up-page.component';
import {FlightsPageComponent} from './pages/flights-page/flights-page.component';
import {AccountSettingsPageComponent} from './pages/account-settings-page/account-settings-page.component';
import {BookedFlightsComponent} from './components/booked-flights/booked-flights.component';
import {PassengersFormComponent} from './components/passengers-form/passengers-form.component';
import {CheckInPageComponent} from './pages/check-in-page/check-in-page.component';
import {SuccessPageComponent} from './pages/success-page/success-page.component';
import {BookingOverviewComponent} from './components/booking-overview/booking-overview.component';
import {AuthGuard} from './auth/auth.guard';
import {FlightSearchPageComponent} from './pages/flight-search-page/flight-search-page.component';


export const appRoutes: Routes = [
    {path: '', component: FlightSearchPageComponent},
    {path: 'settings', component: AccountSettingsPageComponent, canActivate: [AuthGuard]},
    {path: 'flights', component: FlightsPageComponent},
    {path: 'signup', component: SignUpPageComponent},
    {path: 'booked', component: BookedFlightsComponent, canActivate: [AuthGuard]},
    {path: 'passengers', component: PassengersFormComponent, canActivate: [AuthGuard]},
    {path: 'checkin', component: CheckInPageComponent, canActivate: [AuthGuard]},
    {path: 'success', component: SuccessPageComponent, canActivate: [AuthGuard]},
    {path: 'overview', component: BookingOverviewComponent, canActivate: [AuthGuard]},
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
