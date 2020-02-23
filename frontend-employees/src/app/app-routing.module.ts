import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {FlightOverviewComponent} from './components/flight-overview/flight-overview.component';
import {AccountComponent} from './components/account/account.component';
import {TimeTrackingComponent} from './components/time-tracking/time-tracking.component';


const routes: Routes = [
    {path: 'flightoverview', component: FlightOverviewComponent},
    {path: 'account', component: AccountComponent},
    {path: 'time', component: TimeTrackingComponent}
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})

export class AppRoutingModule {
}
