import {Component, OnInit, ViewChild} from '@angular/core';
import {NgForm} from '@angular/forms';
import {Router} from '@angular/router';
import {Observable} from 'rxjs';
import {AirportDto} from '../../services/dtos/Dtos';
import {AirportService} from '../../services/airport/airport.service';

@Component({
    selector: 'app-flight-search',
    templateUrl: './flight-search.component.html',
    styleUrls: ['./flight-search.component.scss']
})
export class FlightSearchComponent implements OnInit {
    options$ = new Observable<AirportDto[]>();

    @ViewChild('searchForm', {static: true}) searchForm !: NgForm;

    constructor(
        private router: Router,
        private airportService: AirportService
    ) {
    }

    ngOnInit(): void {
        this.options$ = this.airportService.getAirports();
    }

    get now() {
        return new Date();
    }

    onSearch() {
        if (this.searchForm.valid) {
            this.router.navigate(['flights']);
        }
    }
}
