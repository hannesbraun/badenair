public class PlaneScheduleDto {
    private final long id;
    private final String plane;
    private final String status;
    private final boolean hasConflict;
    private final FlightDto[] flights;
}