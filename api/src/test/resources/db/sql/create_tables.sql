CREATE TABLE client
(
    client_id INT NOT NULL PRIMARY KEY,
    password VARCHAR(2048) NOT NULL,
    name VARCHAR(2048),
    surname VARCHAR(2048),
    is_admin BOOLEAN NOT NULL,
    logged_until DATETIME NOT NULL
);

CREATE TABLE hall
(
    hall_id INT NOT NULL PRIMARY KEY,
    advance_price DECIMAL(13, 4) NOT NULL, -- total digits 13, after decimal point 4
    total_price DECIMAL(13, 4) NOT NULL,
    screen_size DECIMAL(13, 4) NOT NULL
);

CREATE TABLE seat
(
    seat_id INT NOT NULL PRIMARY KEY,
    row_no INT NOT NULL,
    seat_no INT NOT NULL,
    is_vip BOOLEAN NOT NULL,
    hall_id INT NOT NULL,
    FOREIGN KEY (hall_id) REFERENCES hall(hall_id)
);

CREATE TABLE movie
(
    movie_id INT NOT NULL PRIMARY KEY,
    name VARCHAR(2048) NOT NULL,
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    base_price DECIMAL(13, 4) NOT NULL,
    hall_id INT NOT NULL,
    FOREIGN KEY (hall_id) REFERENCES hall(hall_id)
);

CREATE TABLE hall_reservation
(
    hall_reservation_id INT NOT NULL PRIMARY KEY,
    valid_until DATETIME NOT NULL,
    is_paid_advance BOOLEAN NOT NULL,
    is_paid_total BOOLEAN NOT NULL,
    reservation_date DATE NOT NULL,
    hall_id INT NOT NULL,
    client_id INT NOT NULL,
    FOREIGN KEY (hall_id) REFERENCES hall(hall_id),
    FOREIGN KEY (client_id) REFERENCES client(client_id)
);

CREATE TABLE seat_reservation
(
    seat_reservation_id INT NOT NULL PRIMARY KEY,
    valid_until DATETIME NOT NULL,
    is_paid BOOLEAN NOT NULL,
    movie_id INT NOT NULL,
    seat_id INT NOT NULL,
    client_id INT NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movie(movie_id),
    FOREIGN KEY (seat_id) REFERENCES seat(seat_id),
    FOREIGN KEY (client_id) REFERENCES client(client_id)
);