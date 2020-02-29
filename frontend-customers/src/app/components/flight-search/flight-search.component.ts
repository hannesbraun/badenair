import {Component, ViewChild} from '@angular/core';
import {NgForm} from '@angular/forms';
import {Router} from '@angular/router';

@Component({
    selector: 'app-flight-search',
    templateUrl: './flight-search.component.html',
    styleUrls: ['./flight-search.component.scss']
})
export class FlightSearchComponent {
    options = ['Baden Baden', 'Offenburg'];

    @ViewChild('searchForm', {static: true}) searchForm !: NgForm;

    constructor(private router: Router) {
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
