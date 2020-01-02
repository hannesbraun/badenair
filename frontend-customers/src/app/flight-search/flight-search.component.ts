import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-flight-search',
  templateUrl: './flight-search.component.html',
  styleUrls: ['./flight-search.component.scss']
})
export class FlightSearchComponent implements OnInit {
  options = [ 'Baden Baden', 'Offenburg' ];

  constructor() { }

  ngOnInit() {
  }

}
