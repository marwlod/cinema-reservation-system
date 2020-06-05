INSERT INTO client (client_id, email, password, name, surname, is_admin, logged_until)
    VALUES (5000, 'bob@crs.com', 'test', 'Bob', 'The Regular', 0, STR_TO_DATE('31/12/2099 11:00:00 PM', '%e/%c/%Y %r'));
INSERT INTO client (client_id, email, password, name, surname, is_admin, logged_until)
    VALUES (5005, 'admin@crs.com', 'test', 'Admin', 'The Great', 1, STR_TO_DATE('31/12/2099 11:00:00 PM', '%e/%c/%Y %r'));

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
INSERT INTO movie (movie_id, name, start_date, end_date, base_price, hall_id)
    VALUES (3, 'American Pie 5: Naked Mile', STR_TO_DATE('15/05/2020 8:00:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('15/05/2020 10:00:00 PM', '%e/%c/%Y %r'), 35, 2);
INSERT INTO movie (movie_id, name, start_date, end_date, base_price, hall_id)
    VALUES (4, 'Back to the future III', STR_TO_DATE('20/06/2020 8:00:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('20/06/2020 10:00:00 PM', '%e/%c/%Y %r'), 25, 1);
INSERT INTO movie (movie_id, name, start_date, end_date, base_price, hall_id)
    VALUES (5, 'Cinema Paradiso', STR_TO_DATE('21/06/2020 02:00:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('21/06/2020 04:00:00 PM', '%e/%c/%Y %r'), 30, 2);
INSERT INTO movie (movie_id, name, start_date, end_date, base_price, hall_id)
    VALUES (6, 'The Usual Suspects', STR_TO_DATE('21/06/2020 8:00:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('21/06/2020 10:00:00 PM', '%e/%c/%Y %r'), 30, 2);
INSERT INTO movie (movie_id, name, start_date, end_date, base_price, hall_id)
    VALUES (7, 'The Dark Knight Rises', STR_TO_DATE('22/06/2020 6:00:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('22/06/2020 8:00:00 PM', '%e/%c/%Y %r'), 25, 1);

INSERT INTO special_offers (special_offer_id, code,percentage) VALUES (1, 'A123',10);

INSERT INTO hall_reservation (valid_until, is_paid_advance, is_paid_total, reservation_date, hall_id, client_id, is_deleted)
    VALUES (STR_TO_DATE('15/05/2020 11:59:59 PM', '%e/%c/%Y %r'), 1, 1, STR_TO_DATE('15/05/2020', '%e/%c/%Y %r'), 1, 5000, 0);
INSERT INTO seat_reservation (valid_until, is_paid, movie_id, seat_id, client_id, total_price, is_deleted)
    VALUES (STR_TO_DATE('15/05/2020 10:00:00 PM', '%e/%c/%Y %r'), 1, 3, 200, 5000, 31.50, 0);