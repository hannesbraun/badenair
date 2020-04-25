import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Observable} from 'rxjs';
import {AirportDto} from '../../services/dtos/Dtos';

@Component({
    selector: 'app-flight-search',
    templateUrl: './flight-search.component.html',
    styleUrls: ['./flight-search.component.scss']
})
export class FlightSearchComponent implements OnInit{
    @Input() airports !: Observable<AirportDto[]>;
    @Input() searchValue: any;
    @Output() search = new EventEmitter();


    searchForm: FormGroup = this.formBuilder.group({
        start: ['', Validators.required],
        destination: ['', Validators.required],
        fromDate: ['', Validators.required],
        toDate: ['', Validators.required],
        passengers: ['', [Validators.required, Validators.min(1)]],
        type: ['', Validators.required],
    });

    constructor(private formBuilder: FormBuilder) {
    }

    ngOnInit(): void {
        if (this.searchValue) {
            this.searchForm.setValue(this.searchValue);
        }
    }

    get now() {
        return new Date();
    }

    onSearch() {
        if (this.searchForm.valid) {
            this.search.emit(this.searchForm.value);
        }
    }
}
