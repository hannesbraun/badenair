insert into airport (id, distance, name) values
    (0, 0, 'Baden-Baden'),
    (1, 555, 'Berlin'),
    (2, 142, 'Baseln'),
    (3, 670, 'London'),
    (4, 1322, 'Madrid'),
    (5, 547, 'Hamburg'),
    (6, 1203, 'Palma de Mallorca'),
    (7, 2126, 'Moskau');

insert into standby_schedule (id, end_time, start_time, employee_user_id) values
    (0, NOW(), NOW(), 'Max Musterman'),
    (1, NOW(), NOW(), 'Lukas Has'),
    (2, NOW(), NOW(), 'Lena Peter'),
    (3, NOW(), NOW(), 'Alexander Schmied'),
    (4, NOW(), NOW(), 'Max Müller'),
    (5, NOW(), NOW(), 'Hans Gauß'),
    (6, NOW(), NOW(), 'Laura Muserman'),
    (7, NOW(), NOW(), 'Max Lustig');

insert into scheduled_flight (id, base_price, duration_in_hours, start_time, destination_airport_id, starting_airport_id) values
    (0, 120, 3, NOW(), 1, 0),
    (1, 130, 1, NOW(), 0, 1),
    (2, 115, 2, NOW(), 3, 2),
    (3, 23, 1, NOW(), 2, 3),
    (4, 23, 1, NOW(), 3, 2),
    (5, 23, 1, NOW(), 1, 3);


insert into plane_type_data (id, flight_range, number_of_passengers, type) values
    (4, 2500, 120, 'B737_400'),
    (5, 2500, 50, 'B737_400');

insert into plane (id, state, traveled_distance, type_data_id) values
    (0, 'WAITING', 0, 4),
    (1, 'ON_FLIGHT', 1, 5),
    (2, 'ON_FLIGHT', 2, 4),
    (3, 'ON_FLIGHT', 3, 5);

insert into flight (id, plane_id, actual_start_time, actual_landing_time, scheduled_flight_id) values
    (0, 0, Now(), Now(), 0),
    (1, 1, Now(), Now(), 1),
    (2, 2, Now(), Now(), 2),
    (3, 2, Now(), Now(), 4),
    (4, 2, Now(), Now(), 5),
    (5, 3, Now(), Now(), 3);
