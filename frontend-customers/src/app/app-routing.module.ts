import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule, Routes} from '@angular/router';
import {FlightSearchComponent} from './components/flight-search/flight-search.component';
import {AccountComponent} from './components/account/account.component';
import {FlightOverviewComponent} from './components/flight-overview/flight-overview.component';

export const appRoutes: Routes = [
    { path: '', component: FlightSearchComponent },
    { path: 'account', component: AccountComponent },
    { path: 'flightOverview', component: FlightOverviewComponent }
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
