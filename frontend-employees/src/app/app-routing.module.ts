import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {TimeTrackingComponent} from "./components/time-tracking/time-tracking.component";
import {FlightOverviewComponent} from "./components/flight-overview/flight-overview.component";
import {AccountComponent} from "./components/account/account.component";


const routes: Routes = [
    {path: '', component: TimeTrackingComponent},
    {path: 'flightoverview', component: FlightOverviewComponent},
    {path: 'account', component: AccountComponent}
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})

export class AppRoutingModule {
}
