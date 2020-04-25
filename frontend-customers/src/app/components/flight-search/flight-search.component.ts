import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Observable} from 'rxjs';
import {AirportDto} from '../../services/dtos/Dtos';
import {MatRadioChange} from '@angular/material/radio';

export interface SearchFormValue {
    start: string;
    destination: string;
    fromDate: Date;
    toDate?: Date;
    passengers: number;
    type: string;
}

@Component({
    selector: 'app-flight-search',
    templateUrl: './flight-search.component.html',
    styleUrls: ['./flight-search.component.scss']
})
export class FlightSearchComponent implements OnInit{
    @Input() airports !: Observable<AirportDto[]>;
    @Input() searchValue !: SearchFormValue;
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
            if (this.searchValue.type === '2') {
                this.toggleDisableToDate({value: this.searchValue.type} as MatRadioChange);
            }
            this.searchForm.patchValue(this.searchValue);
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

    toggleDisableToDate(event: MatRadioChange) {
        if (event.value === '2') {
            this.searchForm.controls.toDate.disable();
            return;
        }
        this.searchForm.controls.toDate.enable();
    }
}
