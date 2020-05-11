import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CheckInService {
  
  apiUrl = environment.backendApiRoot;

  constructor(private http : HttpClient) { }

  updateCheckIn(planeId: number, seatNumber: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/flight/checkin/${planeId}`, seatNumber);
    // Todo: Errorhandling
  }
}
