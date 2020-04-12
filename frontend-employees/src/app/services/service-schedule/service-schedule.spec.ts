import {TestBed} from '@angular/core/testing';
import {ServiceScheduleService} from './service-schedule.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {environment} from '../../../environments/environment';

describe('SortService', () => {
    let service: ServiceScheduleService;
    let httpMock: HttpTestingController;
    const testSchedule = {
        startTime: new Date(),
        endTime: new Date(),
        employeeUserId: 'randomId',
    };

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [ HttpClientTestingModule ]
        });
        service = TestBed.inject(ServiceScheduleService);
        httpMock = TestBed.inject(HttpTestingController);
    });

    it('should be created', () => {
        expect(service).toBeTruthy();
    });

    it('should return valid Schedule', (done) => {
        service.getServiceSchedule()
            .subscribe(schedules => {
                const {employee, schedule: {start, end} } = schedules[0];

                expect(employee).toEqual(testSchedule.employeeUserId);
                expect(start).toEqual(testSchedule.startTime);
                expect(end).toEqual(testSchedule.endTime);
                done();
            });

        const apiRequest = httpMock.expectOne(`${environment.backendApiRoot}/plan/standby`);
        apiRequest.flush([testSchedule]);

        httpMock.verify();
    });
});
