import {Component, OnInit, ViewChild} from '@angular/core';
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-flight-search',
  templateUrl: './flight-search.component.html',
  styleUrls: ['./flight-search.component.scss']
})
export class FlightSearchComponent implements OnInit {
  options = [ 'Baden Baden', 'Offenburg' ];

  @ViewChild('searchForm', {static: true}) searchForm: NgForm;

  constructor() { }

  ngOnInit() {
  }

  onSearch() {
    if (this.searchForm.valid) {

    }
  }
}
