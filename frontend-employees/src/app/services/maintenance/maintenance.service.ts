import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { PlaneMaintenance } from '../dtos/Dtos';

@Injectable({
  providedIn: 'root'
})
export class MaintenanceService {
  
  apiUrl = environment.backendApiRoot;

  constructor(private http : HttpClient) { }

  updateMaintenance(planeId: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/maintenance${planeId}`, null);
    // Todo: Errorhandling
  }

  getMaintenanceList(): Observable<PlaneMaintenance[]>{
    return this.http.get<PlaneMaintenance[]>(`${this.apiUrl}/maintenance`);
    // Todo: Errorhandling
  }
}
