import { SecondsToTimePipe } from './seconds-to-time.pipe';

describe('ToTimePipe', () => {
  it('create an instance', () => {
    const pipe = new SecondsToTimePipe();
    expect(pipe).toBeTruthy();
  });
});
