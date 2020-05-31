import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs';
import {CheckInInfoDto} from '../dtos/Dtos';
import {InfoService} from '../info/info.service';

@Injectable({
    providedIn: 'root'
})
export class CheckInService {

    apiUrl = environment.backendApiRoot;

    constructor(private http: HttpClient, private infoService: InfoService) {
    }

    updateCheckIn(planeId: number): Observable<void> {
        return this.http.patch<void>(`${this.apiUrl}/flight/checkin/${planeId}`, {});
    }

    getCheckInInfo(flightId: number): Observable<CheckInInfoDto> {
        return this.http.get<CheckInInfoDto>(`${this.apiUrl}/flight/checkin/${flightId}`);
    }

    downloadPdf(travelerId: number, name: string): void {
        const params = new HttpParams().set('travelerId', String(travelerId));
        this.http.get(`${this.apiUrl}/boardingpass`, {params, responseType: 'blob'})
            .subscribe((pdfData) => {
                const blob = new Blob([pdfData], {type: 'application/pdf'});
                this.downloadBlob(blob, name);
            }, error => this.infoService.showErrorMessage('Der Download konnte nicht abgerufen werden'));
    }

    private downloadBlob(blob: Blob, name: string): void {
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `BoardingPass_${name}.pdf`;
        link.click();
        window.URL.revokeObjectURL(url);
        link.remove();
    }
}
