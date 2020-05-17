import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs';
import {CheckInInfoDto} from '../dtos/Dtos';

@Injectable({
    providedIn: 'root'
})
export class CheckInService {

    apiUrl = environment.backendApiRoot;

    constructor(private http: HttpClient) {
    }

    updateCheckIn(planeId: number): Observable<void> {
        return this.http.patch<void>(`${this.apiUrl}/flight/checkin/${planeId}`, {});
    }

    getCheckInInfo(flightId: number): Observable<CheckInInfoDto> {
        return this.http.get<CheckInInfoDto>(`${this.apiUrl}/flight/checkin/${flightId}`);
    }

    downloadPdf(travelerId: number): void {
        const params = new HttpParams().set('travelerId', String(travelerId));
        this.http.get(`${this.apiUrl}/boardingpass`, {params, responseType: 'blob'})
            .subscribe((pdfData) => {
                const blob = new Blob([pdfData], {type: 'application/pdf'});
                this.downloadBlob(blob);
            });
    }

    private downloadBlob(blob: Blob): void {
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = 'BoardingPass.pdf';
        link.click();
        window.URL.revokeObjectURL(url);
        link.remove();
    }
}
