import {Component, OnInit, ViewChild} from '@angular/core';
import {NgForm} from '@angular/forms';
import {Router} from '@angular/router';

@Component({
    selector: 'app-flight-search',
    templateUrl: './flight-search.component.html',
    styleUrls: ['./flight-search.component.scss']
})
export class FlightSearchComponent implements OnInit {
    options = ['Baden Baden', 'Offenburg'];

    @ViewChild('searchForm', {static: true}) searchForm: NgForm;

    constructor(private router: Router) {
    }

    ngOnInit() {
    }

    onSearch() {
        if (this.searchForm.valid) {
            this.router.navigate(['flightOverview']);
        }
    }
}
