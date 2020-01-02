import {Routes} from '@angular/router';
import {AccountComponent} from "./account/account.component";
import {FlightSearchComponent} from "./flight-search/flight-search.component";
import {FlightOverviewComponent} from "./flight-overview/flight-overview.component";

export const appRoutes: Routes = [

  { path: '', component: FlightSearchComponent },
  { path: 'account', component: AccountComponent },
  { path: 'flightOverview', component: FlightOverviewComponent }
];
