INSERT INTO client (client_id, email, password, name, surname, is_admin, logged_until)
    VALUES (5000, 'email@domain.com', 'secretpass', 'Bob', 'Dough', 0, STR_TO_DATE('31/12/2099 11:00:00 PM', '%e/%c/%Y %r'));

INSERT INTO hall (hall_id, advance_price, total_price, screen_size) VALUES (1, 50, 500, 200);
INSERT INTO seat (seat_id, row_no, seat_no, is_vip, hall_id) VALUES (100, 1, 1, 0, 1);
INSERT INTO seat (seat_id, row_no, seat_no, is_vip, hall_id) VALUES (101, 1, 2, 0, 1);
INSERT INTO seat (seat_id, row_no, seat_no, is_vip, hall_id) VALUES (102, 1, 3, 0, 1);
INSERT INTO seat (seat_id, row_no, seat_no, is_vip, hall_id) VALUES (103, 2, 1, 0, 1);
INSERT INTO seat (seat_id, row_no, seat_no, is_vip, hall_id) VALUES (104, 2, 2, 0, 1);
INSERT INTO seat (seat_id, row_no, seat_no, is_vip, hall_id) VALUES (105, 2, 3, 0, 1);

INSERT INTO hall (hall_id, advance_price, total_price, screen_size) VALUES (2, 100, 1000, 300);
INSERT INTO seat (seat_id, row_no, seat_no, is_vip, hall_id) VALUES (200, 1, 1, 0, 2);
INSERT INTO seat (seat_id, row_no, seat_no, is_vip, hall_id) VALUES (201, 1, 2, 0, 2);
INSERT INTO seat (seat_id, row_no, seat_no, is_vip, hall_id) VALUES (202, 1, 3, 0, 2);
INSERT INTO seat (seat_id, row_no, seat_no, is_vip, hall_id) VALUES (203, 2, 1, 0, 2);
INSERT INTO seat (seat_id, row_no, seat_no, is_vip, hall_id) VALUES (204, 2, 2, 0, 2);
INSERT INTO seat (seat_id, row_no, seat_no, is_vip, hall_id) VALUES (205, 2, 3, 0, 2);
INSERT INTO seat (seat_id, row_no, seat_no, is_vip, hall_id) VALUES (206, 3, 1, 1, 2);
INSERT INTO seat (seat_id, row_no, seat_no, is_vip, hall_id) VALUES (207, 3, 2, 1, 2);
INSERT INTO seat (seat_id, row_no, seat_no, is_vip, hall_id) VALUES (208, 3, 3, 1, 2);

INSERT INTO movie (movie_id, name, start_date, end_date, base_price, hall_id)
    VALUES (1, 'Lion King', STR_TO_DATE('01/01/2030 1:30:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('01/01/2030 3:30:00 PM', '%e/%c/%Y %r'), 20, 1);
INSERT INTO movie (movie_id, name, start_date, end_date, base_price, hall_id)
    VALUES (2, 'Joker', STR_TO_DATE('01/01/2030 8:00:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('01/01/2030 10:00:00 PM', '%e/%c/%Y %r'), 30, 2);

