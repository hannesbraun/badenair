import {Component, EventEmitter, Input, Output} from '@angular/core';
import {MatCalendarCellCssClasses} from '@angular/material/datepicker';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {RequestVacationDto} from '../../services/dtos/Dtos';

export enum VacationState {
    PENDING = 'Ausstehend',
    APPROVED = 'Genehmigt'
}

export interface VacationPlanTableData {
    duration: Date[];
    days: number;
    state: VacationState;
}

@Component({
    selector: 'app-vacation-planning',
    templateUrl: './vacation-planning.component.html',
    styleUrls: ['./vacation-planning.component.scss']
})
export class VacationPlanningComponent {

    static readonly lastPossibleRequestDayOfMonth = 10;

    calendar1: Date;
    calendar2: Date;
    calendar3: Date;

    @Input() vacationDaysLeft !: number;
    @Input() tableData !: VacationPlanTableData[];
    @Input() approvedDates !: number[][];
    @Input() pendingDates !: number[][];

    @Output() requestSubmit = new EventEmitter<RequestVacationDto>();

    displayedColumns: string[] = ['duration', 'days', 'state'];

    requestVacationForm: FormGroup = this.formBuilder.group({
        fromDate: ['', Validators.required],
        toDate: ['', Validators.required]
    });

    constructor(private formBuilder: FormBuilder) {
        const date = (offset: number) => new Date(this.currentDate.getFullYear(), (this.currentDate.getMonth() + offset) % 11);

        this.calendar1 = date(1);
        this.calendar2 = date(2);
        this.calendar3 = date(3);
    }

    get currentDate() {
        return new Date();
    }

    getSelectedDates(calenderNumber: number) {
        const approvedDate = this.approvedDates[calenderNumber];
        const pendingDate = this.pendingDates[calenderNumber];

        return (d: Date): MatCalendarCellCssClasses => {
            const date = d.getDate();

            if (approvedDate?.includes(date)) {
                return 'approved-date';
            }
            if (pendingDate?.includes(date)) {
                return 'pending-date';
            }
            return '';
        };
    }

    onSubmit() {
        if (this.requestVacationForm.valid) {
            const dto: RequestVacationDto = {
                startDate: this.setHourOffset(this.requestVacationForm.get('fromDate')?.value),
                endDate: this.setHourOffset(this.requestVacationForm.get('toDate')?.value)
            };

            this.requestSubmit.emit(dto);
        }
    }

    get canRequestVacation(): boolean {
        const today = new Date();
        return today.getDate() < VacationPlanningComponent.lastPossibleRequestDayOfMonth;
    }

    private setHourOffset(date: Date): Date {
        date.setHours(12, 0, 0);
        return date;
    }
}
