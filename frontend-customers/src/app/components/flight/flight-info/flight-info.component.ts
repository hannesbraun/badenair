import {Component, Input} from '@angular/core';

@Component({
    selector: 'app-flight-info',
    template: `
        <div>
            <p>{{time | date:'dd.MM.yyyy'}}</p>
            <p class="location">{{location}}</p>
            <p>{{time | date:'HH:mm:ss'}} Uhr</p>
        </div>
    `,
    styles: [`
        div {
            display: flex;
            flex-flow: column nowrap;
            align-items: center;
        }

        .location {
            font-size: 1.3em;
            font-weight: bold;
        }
    `]
})
export class FlightInfoComponent {
    @Input() time !: Date;
    @Input() location !: string;
}
