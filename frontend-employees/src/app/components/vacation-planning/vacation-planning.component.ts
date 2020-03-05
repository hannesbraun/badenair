import {Component, OnInit} from '@angular/core';

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

    dataSource = [
        {duration: [new Date(), new Date()], days: 3, state: vacationState.pending},
        {duration: [new Date(), new Date()], days: 4, state: vacationState.approved},
        {duration: [new Date(), new Date()], days: 6, state: vacationState.pending},
    ];

    displayedColumns: string[] = ['duration', 'days', 'state'];

    constructor() {
    }

    ngOnInit(): void {
    }

}
