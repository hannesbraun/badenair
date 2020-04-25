import {Injectable} from '@angular/core';
import {ReplaySubject} from 'rxjs';
import {FlightDto} from '../dtos/Dtos';

@Injectable({
    providedIn: 'root'
})
export class SearchService {

    private readonly searchData: ReplaySubject<FlightDto[]>;

    constructor() {
        this.searchData = new ReplaySubject<FlightDto[]>(1);
    }

    setFlightSearchData(data: FlightDto[]) {
        this.searchData.next(data);
    }

    get flightSearchData() {
        return this.searchData;
    }
}
