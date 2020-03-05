import {Injectable} from '@angular/core';
import {ChangeBaggageStateDto} from '../dtos/Dtos';
import {Observable, of} from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class BaggageService {

    constructor() {
    }

    updateBaggageState(dto: ChangeBaggageStateDto): Observable<void> {
        // TODO: Replace with API call
        return of();
    }
}
