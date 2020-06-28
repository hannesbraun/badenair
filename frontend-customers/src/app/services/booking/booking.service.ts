import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { BookingDto } from '../dtos/Dtos';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class BookingService {

  apiUrl = environment.backendApiRoot;

  constructor(private http: HttpClient) { }

  bookFlight(booking: BookingDto): Observable<number> {
    return this.http.post<number>(`${this.apiUrl}/flight/booking`, booking);
  }

  confirmBooking(bookingIds: number[]): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/flight/confirmBooking`, bookingIds);
  }

}
