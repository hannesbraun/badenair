import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule, Routes} from '@angular/router';
import {SignUpPageComponent} from './pages/sign-up-page/sign-up-page.component';
import {FlightsPageComponent} from './pages/flights-page/flights-page.component';
import {AccountSettingsPageComponent} from './pages/account-settings-page/account-settings-page.component';
import {BookedFlightsComponent} from './components/booked-flights/booked-flights.component';
import {CheckInPageComponent} from './pages/check-in-page/check-in-page.component';
import {SuccessPageComponent} from './pages/success-page/success-page.component';
import {AuthGuard} from './auth/auth.guard';
import {FlightSearchPageComponent} from './pages/flight-search-page/flight-search-page.component';
import {PassengersPageComponent} from './pages/passengers-page/passengers-page.component';
import {SeatSelectionPageComponent} from './pages/seat-selection-page/seat-selection-page.component';
import { BookingOverviewPageComponent } from './pages/booking-overview-page/booking-overview-page.component';


export const appRoutes: Routes = [
    {path: '', component: FlightSearchPageComponent},
    {path: 'settings', component: AccountSettingsPageComponent, canActivate: [AuthGuard]},
    {path: 'flights', component: FlightsPageComponent},
    {path: 'signup', component: SignUpPageComponent},
    {path: 'booked', component: BookedFlightsComponent, canActivate: [AuthGuard]},
    {path: 'passengers', component: PassengersPageComponent, canActivate: [AuthGuard]},
    {path: 'seat', component: SeatSelectionPageComponent, canActivate: [AuthGuard]},
    {path: 'checkin/:id', component: CheckInPageComponent, canActivate: [AuthGuard]},
    {path: 'success', component: SuccessPageComponent, canActivate: [AuthGuard]},
    {path: 'overview', component: BookingOverviewPageComponent, canActivate: [AuthGuard]},
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
