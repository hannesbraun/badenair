import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class StartLandingService {

  apiUrl = environment.backendApiRoot;
  constructor(private http : HttpClient) { }


  updateFlightTracking(flightId: number, action : string){
    return this.http.patch(`${this.apiUrl}/flight/tracking/${flightId}`, action);
    //Todo: Error-handling
  }

}
