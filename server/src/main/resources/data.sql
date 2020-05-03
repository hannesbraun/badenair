insert into scheduled_flight (id, base_price, duration_in_hours, start_time, destination_airport_id, starting_airport_id) values
    (0, 120, 3, NOW(), 1, 0),
    (1, 130, 1, NOW(), 0, 1),
    (2, 115, 2, NOW(), 3, 2),
    (3, 23, 1, NOW(), 2, 3),
    (4, 23, 1, NOW(), 3, 2),
    (5, 23, 1, NOW(), 1, 3);

insert into flight (id, plane_id, actual_start_time, actual_landing_time, scheduled_flight_id) values
    (0, 0, Now(), Now(), 0),
    (1, 1, Now(), Now(), 1),
    (2, 2, Now(), Now(), 2),
    (3, 2, Now(), Now(), 4),
    (4, 2, Now(), Now(), 5),
    (5, 3, Now(), Now(), 3);
