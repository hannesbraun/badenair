import {Component, OnInit} from '@angular/core';
import {MatCalendarCellCssClasses} from '@angular/material/datepicker';

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
    calender2: Date;
    calender3: Date;

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

    constructor() {
    }

    ngOnInit(): void {
        const currentDate = new Date();
        this.calendar1 = new Date(currentDate.getFullYear(), (currentDate.getMonth() + 1) % 11);
        this.calendar2 = new Date(currentDate.getFullYear(), (currentDate.getMonth() + 2) % 11);
        this.calendar3 = new Date(currentDate.getFullYear(), (currentDate.getMonth() + 3) % 11);
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
}
