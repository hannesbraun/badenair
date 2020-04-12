import {Component, OnInit} from '@angular/core';
import {VacationService} from '../../services/vacation/vacation.service';
import {VacationPlanTableData, VacationState} from '../../components/vacation-planning/vacation-planning.component';
import {RequestVacationDto} from '../../services/dtos/Dtos';

@Component({
    selector: 'app-vacation-planning-page',
    templateUrl: './vacation-planning-page.component.html',
    styleUrls: ['./vacation-planning-page.component.scss']
})
export class VacationPlanningPageComponent implements OnInit {

    private readonly DAY_IN_MS = 1000 * 60 * 60 * 24;

    tableData: VacationPlanTableData[] = [];
    approvedDates: number[][] = [];
    pendingDates: number[][] = [];
    loading = false;

    constructor(private vacationService: VacationService) {
    }

    ngOnInit(): void {
        this.vacationService.getVacationPlan().subscribe(vacationPlan => {
            this.tableData = vacationPlan.map(vacation => {
                return {
                    duration: [vacation.startDate, vacation.endDate],
                    days: Math.ceil((vacation.endDate.getTime() - vacation.startDate.getTime()) / this.DAY_IN_MS),
                    state: VacationState.APPROVED
                } as VacationPlanTableData;
            });

            if (vacationPlan.length > 0) {
                const startMonth = vacationPlan[0].startDate.getMonth();
                const dates = vacationPlan
                    .map(vacation => {
                        let temp = vacation.startDate;
                        const dateRange: Date[] = [];
                        while (temp.getTime() < vacation.endDate.getTime()) {
                            dateRange.push(temp);
                            temp = new Date(temp.getTime() + this.DAY_IN_MS);
                        }

                        dateRange.push(vacation.endDate);

                        return dateRange;
                    })
                    .reduce(this.concat, []);

                for (let i = 0; i < 4; i++) {
                    this.approvedDates[i] = dates
                        .filter(date => date.getMonth() === ((startMonth + i) % 12))
                        .map(date => date.getDate());
                }
            }

            this.loading = true;
        });
    }

    onVacationRequest(dto: RequestVacationDto) {
        this.vacationService.requestVacation(dto).subscribe(() => {
            this.loading = false;
            this.ngOnInit();
        });
    }

    private concat(x: any[], y: any[]) {
        return x.concat(y);
    }
}
