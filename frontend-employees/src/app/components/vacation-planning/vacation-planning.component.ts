import {Component, OnInit} from '@angular/core';
import {MatCalendarCellCssClasses} from '@angular/material/datepicker';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

enum vacationState {
    pending = 'Ausstehend',
    approved = 'Genehmigt'
}

@Component({
    selector: 'app-vacation-planning',
    templateUrl: './vacation-planning.component.html',
    styleUrls: ['./vacation-planning.component.scss']
})
export class VacationPlanningComponent implements OnInit {

    calendar1: Date;
    calendar2: Date;
    calendar3: Date;

    vacationDays = 13;
    vacationDaysLeft = 9;

    dataSource = [
        {duration: [new Date(), new Date()], days: 3, state: vacationState.pending},
        {duration: [new Date(), new Date()], days: 4, state: vacationState.approved},
        {duration: [new Date(), new Date()], days: 6, state: vacationState.pending},
    ];
    displayedColumns: string[] = ['duration', 'days', 'state'];

    approvedDates = [
        [2, 3, 4, 5, 6, 7],
        [7, 8, 9, 10, 11, 12, 13],
        [13, 14],
        [23, 24, 25]
    ];
    pendingDates = [
        [1],
        [24, 25],
        [],
        [12, 13, 14, 15, 16]
    ];

    requestVacationForm: FormGroup = this.formBuilder.group({
        fromDate: ['', Validators.required],
        toDate: ['', Validators.required],
        fromTimeOfDay: ['', Validators.required],
        toTimeOfDay: ['', Validators.required]
    });

    constructor(private formBuilder: FormBuilder) {
        const date = (offset: number) => new Date(this.currentDate.getFullYear(), (this.currentDate.getMonth() + offset) % 11);

        this.calendar1 = date(1);
        this.calendar2 = date(2);
        this.calendar3 = date(3);
    }

    ngOnInit(): void {
    }

    get currentDate() {
        return new Date();
    }

    getSelectedDates(calenderNumber: number) {
        const approvedDate = this.approvedDates[calenderNumber];
        const pendingDate = this.pendingDates[calenderNumber];

        return (d: Date): MatCalendarCellCssClasses => {
            const date = d.getDate();

            if (approvedDate.includes(date)) {
                return 'approved-date';
            }
            if (pendingDate.includes(date)) {
                return 'pending-date';
            }
            return '';
        };
    }

    onSubmit() {
        if (this.requestVacationForm.valid) {
            // TODO implement on Submit
        }
    }
}
