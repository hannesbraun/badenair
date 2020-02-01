import { Pipe, PipeTransform } from '@angular/core';

const SECONDS_PER_HOUR = 3600;
const HOURS_PER_DAY = 24;
const SECONDS_PER_MINUTE = 60;
const MINUTE_PER_HOUR = 60;

@Pipe({
  name: 'secondsToTime'
})
export class SecondsToTimePipe implements PipeTransform {

    transform(value: number): string {
      const hours = Math.floor(value / SECONDS_PER_HOUR) % HOURS_PER_DAY;
      const minutes =  Math.floor(value / SECONDS_PER_MINUTE) % MINUTE_PER_HOUR;
      const seconds = value % SECONDS_PER_MINUTE;
      return `${ hours < 10 ? '0' + hours : hours }:${ minutes < 10 ? '0' + minutes : minutes }:${ seconds < 10 ? '0' + seconds : seconds }`;
  }
}
