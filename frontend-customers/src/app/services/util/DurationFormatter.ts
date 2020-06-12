export function formatDuration(totalMilliseconds: number) {
    let hours = Math.trunc(totalMilliseconds / 3600000);
    let minutes = Math.trunc(totalMilliseconds / 60000 - hours * 60);
    
    return hours.toString().padStart(2, "0") + ":" + minutes.toString().padStart(2, "0");
}
