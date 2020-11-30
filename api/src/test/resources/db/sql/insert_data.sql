-- TODO remove ugly hardcoded dates

INSERT INTO client (client_id, email, password, name, surname, is_admin, logged_until, register_date)
    VALUES (5000, 'bob@crs.com', 'test', 'Bob', 'The Regular', 0,STR_TO_DATE('31/12/2099 11:00:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('01/01/2020 11:00:00 PM', '%e/%c/%Y %r'));
INSERT INTO client (client_id, email, password, name, surname, is_admin, logged_until, register_date)
    VALUES (5005, 'admin@crs.com', 'test', 'Admin', 'The Great', 1,STR_TO_DATE('31/12/2099 11:00:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('01/01/2020 11:00:00 PM', '%e/%c/%Y %r'));
INSERT INTO client (client_id, email, password, name, surname, is_admin, logged_until, register_date)
    VALUES (5100, 'scarlet@johansson.com', 'test', 'Scarlett', 'Johansson', 0, STR_TO_DATE('31/12/2099 11:00:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('04/06/2020 11:00:00 PM', '%e/%c/%Y %r'));
INSERT INTO client (client_id, email, password, name, surname, is_admin, logged_until, register_date)
    VALUES (5101, 'arnold@schwarzenegger.com', 'test', 'Arnold', 'Schwarzenegger', 0, STR_TO_DATE('31/12/2099 11:00:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('05/06/2020 11:00:00 PM', '%e/%c/%Y %r'));
INSERT INTO client (client_id, email, password, name, surname, is_admin, logged_until, register_date)
    VALUES (5102, 'steven@soderbergh.com', 'test', 'Steven', 'Soderbergh', 0, STR_TO_DATE('31/12/2099 11:00:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('05/06/2020 11:00:00 PM', '%e/%c/%Y %r'));
INSERT INTO client (client_id, email, password, name, surname, is_admin, logged_until, register_date)
    VALUES (5103, 'martin@scorsese.com', 'test', 'Martin', 'Scorsese', 0, STR_TO_DATE('31/12/2099 11:00:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('06/06/2020 11:00:00 PM', '%e/%c/%Y %r'));
INSERT INTO client (client_id, email, password, name, surname, is_admin, logged_until, register_date)
    VALUES (5104, 'roman@polanski.com', 'test', 'Roman', 'Polanski', 0, STR_TO_DATE('31/12/2099 11:00:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('06/06/2020 11:00:00 PM', '%e/%c/%Y %r'));
INSERT INTO client (client_id, email, password, name, surname, is_admin, logged_until, register_date)
    VALUES (5105, 'steven@spielberg.com', 'test', 'Steven', 'Spielberg', 0, STR_TO_DATE('31/12/2099 11:00:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('06/06/2020 11:00:00 PM', '%e/%c/%Y %r'));
INSERT INTO client (client_id, email, password, name, surname, is_admin, logged_until, register_date)
    VALUES (5106, 'robert@zemeckis.com', 'test', 'Robert', 'Zemeckis', 0, STR_TO_DATE('31/12/2099 11:00:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('07/07/2020 11:00:00 PM', '%e/%c/%Y %r'));
INSERT INTO client (client_id, email, password, name, surname, is_admin, logged_until, register_date)
    VALUES (5107, 'david@lynch.com', 'test', 'David', 'Lynch', 0, STR_TO_DATE('31/12/2099 11:00:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('07/06/2020 11:00:00 PM', '%e/%c/%Y %r'));

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

INSERT INTO hall (hall_id, advance_price, total_price, screen_size) VALUES (3, 75, 750, 250);
INSERT INTO seat (seat_id, row_no, seat_no, is_vip, hall_id) VALUES (300, 1, 1, 0, 3);
INSERT INTO seat (seat_id, row_no, seat_no, is_vip, hall_id) VALUES (301, 1, 2, 0, 3);
INSERT INTO seat (seat_id, row_no, seat_no, is_vip, hall_id) VALUES (302, 1, 3, 0, 3);
INSERT INTO seat (seat_id, row_no, seat_no, is_vip, hall_id) VALUES (303, 2, 1, 1, 3);
INSERT INTO seat (seat_id, row_no, seat_no, is_vip, hall_id) VALUES (304, 2, 2, 1, 3);
INSERT INTO seat (seat_id, row_no, seat_no, is_vip, hall_id) VALUES (305, 2, 3, 1, 3);

INSERT INTO hall (hall_id, advance_price, total_price, screen_size) VALUES (4, 150, 1500, 400);
INSERT INTO seat (seat_id, row_no, seat_no, is_vip, hall_id) VALUES (400, 1, 1, 1, 4);
INSERT INTO seat (seat_id, row_no, seat_no, is_vip, hall_id) VALUES (401, 1, 2, 1, 4);
INSERT INTO seat (seat_id, row_no, seat_no, is_vip, hall_id) VALUES (402, 1, 3, 1, 4);
INSERT INTO seat (seat_id, row_no, seat_no, is_vip, hall_id) VALUES (403, 2, 1, 1, 4);
INSERT INTO seat (seat_id, row_no, seat_no, is_vip, hall_id) VALUES (404, 2, 2, 1, 4);
INSERT INTO seat (seat_id, row_no, seat_no, is_vip, hall_id) VALUES (405, 2, 3, 1, 4);

INSERT INTO movie (movie_id, name, start_date, end_date, base_price, hall_id, description, link)
    VALUES (1, 'Lion King', STR_TO_DATE('01/01/2030 1:30:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('01/01/2030 3:30:00 PM', '%e/%c/%Y %r'), 20, 1,
            N'The Lion King is a 2019 American musical drama film directed and produced by Jon Favreau, written by Jeff Nathanson, and produced by Walt Disney Pictures. It is a photorealistic computer-animated remake of Disney''s traditionally animated 1994 film of the same name. The film stars the voices of Donald Glover, Seth Rogen, Chiwetel Ejiofor, Alfre Woodard, Billy Eichner, John Kani, John Oliver, Florence Kasumba, Eric Andre, Keegan-Michael Key, JD McCrary, Shahadi Wright Joseph, and Beyoncé Knowles-Carter, as well as James Earl Jones reprising his role from the original film. The plot follows Simba, a young lion who must embrace his role as the rightful king of his native land following the murder of his father, Mufasa, at the hands of his uncle, Scar.', 'https://lumiere-a.akamaihd.net/v1/images/pr_thelionking2019_digital_18276_ad798a8d.png');
INSERT INTO movie (movie_id, name, start_date, end_date, base_price, hall_id, description, link)
    VALUES (2, 'Joker', STR_TO_DATE('01/01/2030 8:00:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('01/01/2030 10:00:00 PM', '%e/%c/%Y %r'), 30, 2,
            'In Gotham City, mentally troubled comedian Arthur Fleck is disregarded and mistreated by society. He then embarks on a downward spiral of revolution and bloody crime. This path brings him face-to-face with his alter-ego: the Joker.', 'https://fwcdn.pl/fpo/01/67/810167/7905225.3.jpg');
INSERT INTO movie (movie_id, name, start_date, end_date, base_price, hall_id, description, link)
    VALUES (3, 'Pulp fiction', STR_TO_DATE('15/05/2020 8:00:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('15/05/2020 10:00:00 PM', '%e/%c/%Y %r'), 35, 2,
            'The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption. Jules Winnfield (Samuel L. Jackson) and Vincent Vega (John Travolta) are two hit men who are out to retrieve a suitcase stolen from their employer, mob boss Marsellus Wallace (Ving Rhames). Wallace has also asked Vincent to take his wife Mia (Uma Thurman) out a few days later when Wallace himself will be out of town. Butch Coolidge (Bruce Willis) is an aging boxer who is paid by Wallace to lose his fight. The lives of these seemingly unrelated people are woven together comprising of a series of funny, bizarre and uncalled-for incidents.', 'https://i0.wp.com/oldcamera.pl/wp-content/uploads/2018/10/pulp-fiction.jpg?fit=1055%2C1536&ssl=1');
INSERT INTO movie (movie_id, name, start_date, end_date, base_price, hall_id, description, link)
    VALUES (4, 'Back to the future III', STR_TO_DATE('10/06/2020 8:00:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('10/06/2020 10:00:00 PM', '%e/%c/%Y %r'), 25, 1,
            'Back to the Future Part III is a 1990 American science-fiction film[4] and the final installment of the Back to the Future trilogy. The film was directed by Robert Zemeckis, and stars Michael J. Fox, Christopher Lloyd, Mary Steenburgen, Thomas F. Wilson and Lea Thompson. The film continues immediately following Back to the Future Part II (1989); while stranded in 1955 during his time travel adventures, Marty McFly (Fox) discovers that his friend Dr. Emmett "Doc" Brown (Lloyd), trapped in 1885, was killed by Buford "Mad Dog" Tannen (Wilson), Biff''s great-grandfather. Marty travels to 1885 to rescue Doc and return once again to 1985, but matters are complicated when Doc falls in love with Clara Clayton (Steenburgen).', 'https://i.ibb.co/D1p87hy/Back-To-The-Future-III.jpg');
INSERT INTO movie (movie_id, name, start_date, end_date, base_price, hall_id, description, link)
    VALUES (5, 'Cinema Paradiso', STR_TO_DATE('11/06/2020 02:00:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('11/06/2020 04:00:00 PM', '%e/%c/%Y %r'), 30, 2,
            'A filmmaker recalls his childhood when falling in love with the pictures at the cinema of his home village and forms a deep friendship with the cinema''s projectionist. A boy who grew up in a native Sicilian Village returns home as a famous director after receiving news about the death of an old friend. Told in a flashback, Salvatore reminiscences about his childhood and his relationship with Alfredo, a projectionist at Cinema Paradiso. Under the fatherly influence of Alfredo, Salvatore fell in love with film making, with the duo spending many hours discussing about films and Alfredo painstakingly teaching Salvatore the skills that became a stepping stone for the young boy into the world of film making. The film brings the audience through the changes in cinema and the dying trade of traditional film making, editing and screening. It also explores a young boy''s dream of leaving his little town to foray into the world outside.', 'https://images-na.ssl-images-amazon.com/images/I/51fEn3hUzcL._AC_.jpg');
INSERT INTO movie (movie_id, name, start_date, end_date, base_price, hall_id, description, link)
    VALUES (6, 'The Usual Suspects', STR_TO_DATE('11/06/2020 8:00:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('11/06/2020 10:00:00 PM', '%e/%c/%Y %r'), 30, 2,
            N'A sole survivor tells of the twisty events leading up to a horrific gun battle on a boat, which began when five criminals met at a seemingly random police lineup. Following a truck hijack in New York, five criminals are arrested and brought together for questioning. As none of them are guilty, they plan a revenge operation against the police. The operation goes well, but then the influence of a legendary mastermind criminal called Keyser Söze is felt. It becomes clear that each one of them has wronged Söze at some point and must pay back now. The payback job leaves 27 men dead in a boat explosion, but the real question arises now: Who actually is Keyser Söze?', 'https://m.media-amazon.com/images/M/MV5BYTViNjMyNmUtNDFkNC00ZDRlLThmMDUtZDU2YWE4NGI2ZjVmXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_.jpg');
INSERT INTO movie (movie_id, name, start_date, end_date, base_price, hall_id, description, link)
    VALUES (7, 'The Dark Knight Rises', STR_TO_DATE('12/06/2020 6:00:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('12/06/2020 8:00:00 PM', '%e/%c/%Y %r'), 25, 1,
            'Eight years after the Joker''s reign of anarchy, Batman, with the help of the enigmatic Catwoman, is forced from his exile to save Gotham City from the brutal guerrilla terrorist Bane. Despite his tarnished reputation after the events of The Dark Knight (2008), in which he took the rap for Dent''s crimes, Batman feels compelled to intervene to assist the city and its Police force, which is struggling to cope with Banes plans to destroy the city.', 'https://rockmetalshop.pl/pol_pl_plakat-BATMAN-THE-DARK-KNIGHT-RISES-BANE-141484_1.jpg');
INSERT INTO movie (movie_id, name, start_date, end_date, base_price, hall_id, description, link)
    VALUES (8, 'Godfather III', STR_TO_DATE('13/06/2020 2:00:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('13/06/2020 4:00:00 PM', '%e/%c/%Y %r'), 30, 3,
            'In the midst of trying to legitimize his business dealings in New York City and Italy in 1979, aging Mafia Don Michael Corleone seeks to avow for his sins, while taking his nephew Vincent Mancini under his wing. In the final installment of the Godfather Trilogy, an aging Don Michael Corleone seeks to legitimize his crime family''s interests and remove himself from the violent underworld but is kept back by the ambitions of the young. While he attempts to link the Corleone''s finances with the Vatican, Michael must deal with the machinations of a hungrier gangster seeking to upset the existing Mafioso order and a young protege''s love affair with his daughter.', 'https://images-na.ssl-images-amazon.com/images/I/81ttMIxlp1L._SL1500_.jpg');
INSERT INTO movie (movie_id, name, start_date, end_date, base_price, hall_id, description, link)
    VALUES (9, 'Godfather III', STR_TO_DATE('13/06/2020 6:00:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('13/06/2020 8:00:00 PM', '%e/%c/%Y %r'), 30, 3,
            'In the midst of trying to legitimize his business dealings in New York City and Italy in 1979, aging Mafia Don Michael Corleone seeks to avow for his sins, while taking his nephew Vincent Mancini under his wing. In the final installment of the Godfather Trilogy, an aging Don Michael Corleone seeks to legitimize his crime family''s interests and remove himself from the violent underworld but is kept back by the ambitions of the young. While he attempts to link the Corleone''s finances with the Vatican, Michael must deal with the machinations of a hungrier gangster seeking to upset the existing Mafioso order and a young protege''s love affair with his daughter.', 'https://images-na.ssl-images-amazon.com/images/I/81ttMIxlp1L._SL1500_.jpg');
INSERT INTO movie (movie_id, name, start_date, end_date, base_price, hall_id, description, link)
    VALUES (10, 'Godfather III', STR_TO_DATE('03/06/2020 6:00:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('03/06/2020 8:00:00 PM', '%e/%c/%Y %r'), 30, 3,
            'In the midst of trying to legitimize his business dealings in New York City and Italy in 1979, aging Mafia Don Michael Corleone seeks to avow for his sins, while taking his nephew Vincent Mancini under his wing. In the final installment of the Godfather Trilogy, an aging Don Michael Corleone seeks to legitimize his crime family''s interests and remove himself from the violent underworld but is kept back by the ambitions of the young. While he attempts to link the Corleone''s finances with the Vatican, Michael must deal with the machinations of a hungrier gangster seeking to upset the existing Mafioso order and a young protege''s love affair with his daughter.', 'https://images-na.ssl-images-amazon.com/images/I/81ttMIxlp1L._SL1500_.jpg');
INSERT INTO movie (movie_id, name, start_date, end_date, base_price, hall_id, description, link)
    VALUES (11, 'Godfather III', STR_TO_DATE('04/06/2020 6:00:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('04/06/2020 8:00:00 PM', '%e/%c/%Y %r'), 30, 3,
            'In the midst of trying to legitimize his business dealings in New York City and Italy in 1979, aging Mafia Don Michael Corleone seeks to avow for his sins, while taking his nephew Vincent Mancini under his wing. In the final installment of the Godfather Trilogy, an aging Don Michael Corleone seeks to legitimize his crime family''s interests and remove himself from the violent underworld but is kept back by the ambitions of the young. While he attempts to link the Corleone''s finances with the Vatican, Michael must deal with the machinations of a hungrier gangster seeking to upset the existing Mafioso order and a young protege''s love affair with his daughter.', 'https://images-na.ssl-images-amazon.com/images/I/81ttMIxlp1L._SL1500_.jpg');
INSERT INTO movie (movie_id, name, start_date, end_date, base_price, hall_id, description, link)
    VALUES (12, 'Pulp Fiction', STR_TO_DATE('05/06/2020 3:00:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('05/06/2020 5:00:00 PM', '%e/%c/%Y %r'), 30, 3,
            'The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption. Jules Winnfield (Samuel L. Jackson) and Vincent Vega (John Travolta) are two hit men who are out to retrieve a suitcase stolen from their employer, mob boss Marsellus Wallace (Ving Rhames). Wallace has also asked Vincent to take his wife Mia (Uma Thurman) out a few days later when Wallace himself will be out of town. Butch Coolidge (Bruce Willis) is an aging boxer who is paid by Wallace to lose his fight. The lives of these seemingly unrelated people are woven together comprising of a series of funny, bizarre and uncalled-for incidents.', 'https://i0.wp.com/oldcamera.pl/wp-content/uploads/2018/10/pulp-fiction.jpg?fit=1055%2C1536&ssl=1');
INSERT INTO movie (movie_id, name, start_date, end_date, base_price, hall_id, description, link)
    VALUES (13, 'Pulp Fiction', STR_TO_DATE('05/06/2020 6:00:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('05/06/2020 8:00:00 PM', '%e/%c/%Y %r'), 30, 3,
            'The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption. Jules Winnfield (Samuel L. Jackson) and Vincent Vega (John Travolta) are two hit men who are out to retrieve a suitcase stolen from their employer, mob boss Marsellus Wallace (Ving Rhames). Wallace has also asked Vincent to take his wife Mia (Uma Thurman) out a few days later when Wallace himself will be out of town. Butch Coolidge (Bruce Willis) is an aging boxer who is paid by Wallace to lose his fight. The lives of these seemingly unrelated people are woven together comprising of a series of funny, bizarre and uncalled-for incidents.', 'https://i0.wp.com/oldcamera.pl/wp-content/uploads/2018/10/pulp-fiction.jpg?fit=1055%2C1536&ssl=1');
INSERT INTO movie (movie_id, name, start_date, end_date, base_price, hall_id, description, link)
    VALUES (14, 'Cinema Paradiso', STR_TO_DATE('06/06/2020 2:00:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('06/06/2020 4:00:00 PM', '%e/%c/%Y %r'), 30, 3,
            'A filmmaker recalls his childhood when falling in love with the pictures at the cinema of his home village and forms a deep friendship with the cinema''s projectionist. A boy who grew up in a native Sicilian Village returns home as a famous director after receiving news about the death of an old friend. Told in a flashback, Salvatore reminiscences about his childhood and his relationship with Alfredo, a projectionist at Cinema Paradiso. Under the fatherly influence of Alfredo, Salvatore fell in love with film making, with the duo spending many hours discussing about films and Alfredo painstakingly teaching Salvatore the skills that became a stepping stone for the young boy into the world of film making. The film brings the audience through the changes in cinema and the dying trade of traditional film making, editing and screening. It also explores a young boy''s dream of leaving his little town to foray into the world outside.', 'https://images-na.ssl-images-amazon.com/images/I/51fEn3hUzcL._AC_.jpg');
INSERT INTO movie (movie_id, name, start_date, end_date, base_price, hall_id, description, link)
    VALUES (15, 'The Usual Suspects', STR_TO_DATE('06/06/2020 6:00:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('06/06/2020 8:00:00 PM', '%e/%c/%Y %r'), 30, 3,
            N'A sole survivor tells of the twisty events leading up to a horrific gun battle on a boat, which began when five criminals met at a seemingly random police lineup. Following a truck hijack in New York, five criminals are arrested and brought together for questioning. As none of them are guilty, they plan a revenge operation against the police. The operation goes well, but then the influence of a legendary mastermind criminal called Keyser Söze is felt. It becomes clear that each one of them has wronged Söze at some point and must pay back now. The payback job leaves 27 men dead in a boat explosion, but the real question arises now: Who actually is Keyser Söze?', 'https://m.media-amazon.com/images/M/MV5BYTViNjMyNmUtNDFkNC00ZDRlLThmMDUtZDU2YWE4NGI2ZjVmXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_.jpg');
INSERT INTO movie (movie_id, name, start_date, end_date, base_price, hall_id, description, link)
    VALUES (16, 'Back to the future III', STR_TO_DATE('07/06/2020 6:00:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('07/06/2020 8:00:00 PM', '%e/%c/%Y %r'), 30, 3,
            'Back to the Future Part III is a 1990 American science-fiction film[4] and the final installment of the Back to the Future trilogy. The film was directed by Robert Zemeckis, and stars Michael J. Fox, Christopher Lloyd, Mary Steenburgen, Thomas F. Wilson and Lea Thompson. The film continues immediately following Back to the Future Part II (1989); while stranded in 1955 during his time travel adventures, Marty McFly (Fox) discovers that his friend Dr. Emmett "Doc" Brown (Lloyd), trapped in 1885, was killed by Buford "Mad Dog" Tannen (Wilson), Biff''s great-grandfather. Marty travels to 1885 to rescue Doc and return once again to 1985, but matters are complicated when Doc falls in love with Clara Clayton (Steenburgen).', 'https://i.ibb.co/D1p87hy/Back-To-The-Future-III.jpg');
INSERT INTO movie (movie_id, name, start_date, end_date, base_price, hall_id, description, link)
    VALUES (17, 'Back to the future III', STR_TO_DATE('07/06/2020 6:00:00 PM', '%e/%c/%Y %r'), STR_TO_DATE('07/06/2020 8:00:00 PM', '%e/%c/%Y %r'), 30, 3,
            'Back to the Future Part III is a 1990 American science-fiction film[4] and the final installment of the Back to the Future trilogy. The film was directed by Robert Zemeckis, and stars Michael J. Fox, Christopher Lloyd, Mary Steenburgen, Thomas F. Wilson and Lea Thompson. The film continues immediately following Back to the Future Part II (1989); while stranded in 1955 during his time travel adventures, Marty McFly (Fox) discovers that his friend Dr. Emmett "Doc" Brown (Lloyd), trapped in 1885, was killed by Buford "Mad Dog" Tannen (Wilson), Biff''s great-grandfather. Marty travels to 1885 to rescue Doc and return once again to 1985, but matters are complicated when Doc falls in love with Clara Clayton (Steenburgen).', 'https://i.ibb.co/D1p87hy/Back-To-The-Future-III.jpg');

INSERT INTO hall_reservation (valid_until, is_paid_advance, is_paid_total, reservation_date, hall_id, client_id, is_deleted)
    VALUES (STR_TO_DATE('15/05/2020 11:59:59 PM', '%e/%c/%Y %r'), 1, 1, STR_TO_DATE('15/05/2020', '%e/%c/%Y %r'), 1, 5000, 0);
INSERT INTO hall_reservation (valid_until, is_paid_advance, is_paid_total, reservation_date, hall_id, client_id, is_deleted)
    VALUES (STR_TO_DATE('03/06/2020 11:59:59 PM', '%e/%c/%Y %r'), 1, 1, STR_TO_DATE('03/06/2020', '%e/%c/%Y %r'), 1, 5100, 0);
INSERT INTO hall_reservation (valid_until, is_paid_advance, is_paid_total, reservation_date, hall_id, client_id, is_deleted)
    VALUES (STR_TO_DATE('03/06/2020 11:59:59 PM', '%e/%c/%Y %r'), 1, 1, STR_TO_DATE('03/06/2020', '%e/%c/%Y %r'), 2, 5101, 0);
INSERT INTO hall_reservation (valid_until, is_paid_advance, is_paid_total, reservation_date, hall_id, client_id, is_deleted)
    VALUES (STR_TO_DATE('04/06/2020 11:59:59 PM', '%e/%c/%Y %r'), 1, 1, STR_TO_DATE('04/06/2020', '%e/%c/%Y %r'), 1, 5102, 0);
INSERT INTO hall_reservation (valid_until, is_paid_advance, is_paid_total, reservation_date, hall_id, client_id, is_deleted)
    VALUES (STR_TO_DATE('04/06/2020 11:59:59 PM', '%e/%c/%Y %r'), 1, 1, STR_TO_DATE('04/06/2020', '%e/%c/%Y %r'), 2, 5102, 0);
INSERT INTO hall_reservation (valid_until, is_paid_advance, is_paid_total, reservation_date, hall_id, client_id, is_deleted)
    VALUES (STR_TO_DATE('06/06/2020 11:59:59 PM', '%e/%c/%Y %r'), 1, 1, STR_TO_DATE('06/06/2020', '%e/%c/%Y %r'), 1, 5103, 0);
INSERT INTO hall_reservation (valid_until, is_paid_advance, is_paid_total, reservation_date, hall_id, client_id, is_deleted)
    VALUES (STR_TO_DATE('06/06/2020 11:59:59 PM', '%e/%c/%Y %r'), 1, 1, STR_TO_DATE('07/06/2020', '%e/%c/%Y %r'), 2, 5103, 1);
INSERT INTO hall_reservation (valid_until, is_paid_advance, is_paid_total, reservation_date, hall_id, client_id, is_deleted)
    VALUES (STR_TO_DATE('06/06/2020 11:59:59 PM', '%e/%c/%Y %r'), 1, 1, STR_TO_DATE('07/06/2020', '%e/%c/%Y %r'), 1, 5104, 1);
INSERT INTO hall_reservation (valid_until, is_paid_advance, is_paid_total, reservation_date, hall_id, client_id, is_deleted)
    VALUES (STR_TO_DATE('07/06/2020 11:59:59 PM', '%e/%c/%Y %r'), 1, 1, STR_TO_DATE('08/06/2020', '%e/%c/%Y %r'), 2, 5105, 1);
INSERT INTO hall_reservation (valid_until, is_paid_advance, is_paid_total, reservation_date, hall_id, client_id, is_deleted)
    VALUES (STR_TO_DATE('08/06/2020 11:59:59 PM', '%e/%c/%Y %r'), 1, 1, STR_TO_DATE('08/06/2020', '%e/%c/%Y %r'), 1, 5106, 0);

INSERT INTO seat_reservation (valid_until, is_paid, movie_id, seat_id, client_id, total_price, is_deleted)
    VALUES (STR_TO_DATE('15/05/2020 10:00:00 PM', '%e/%c/%Y %r'), 1, 3, 200, 5000, 31.50, 0);
INSERT INTO seat_reservation (valid_until, is_paid, movie_id, seat_id, client_id, total_price, is_deleted)
    VALUES (STR_TO_DATE('03/06/2020 8:00:00 PM', '%e/%c/%Y %r'), 1, 10, 300, 5100, 30, 0);
INSERT INTO seat_reservation (valid_until, is_paid, movie_id, seat_id, client_id, total_price, is_deleted)
    VALUES (STR_TO_DATE('03/06/2020 8:00:00 PM', '%e/%c/%Y %r'), 1, 10, 301, 5101, 30, 0);
INSERT INTO seat_reservation (valid_until, is_paid, movie_id, seat_id, client_id, total_price, is_deleted)
    VALUES (STR_TO_DATE('03/06/2020 8:00:00 PM', '%e/%c/%Y %r'), 1, 10, 302, 5102, 30, 0);
INSERT INTO seat_reservation (valid_until, is_paid, movie_id, seat_id, client_id, total_price, is_deleted)
    VALUES (STR_TO_DATE('05/06/2020 5:00:00 PM', '%e/%c/%Y %r'), 1, 12, 301, 5103, 30, 0);
INSERT INTO seat_reservation (valid_until, is_paid, movie_id, seat_id, client_id, total_price, is_deleted)
    VALUES (STR_TO_DATE('05/06/2020 10:00:00 PM', '%e/%c/%Y %r'), 1, 12, 302, 5104, 30, 0);
INSERT INTO seat_reservation (valid_until, is_paid, movie_id, seat_id, client_id, total_price, is_deleted)
    VALUES (STR_TO_DATE('07/06/2020 8:00:00 PM', '%e/%c/%Y %r'), 1, 16, 301, 5100, 30, 0);
INSERT INTO seat_reservation (valid_until, is_paid, movie_id, seat_id, client_id, total_price, is_deleted)
    VALUES (STR_TO_DATE('07/06/2020 8:00:00 PM', '%e/%c/%Y %r'), 1, 16, 302, 5101, 30, 0);
INSERT INTO seat_reservation (valid_until, is_paid, movie_id, seat_id, client_id, total_price, is_deleted)
    VALUES (STR_TO_DATE('07/06/2020 8:00:00 PM', '%e/%c/%Y %r'), 1, 16, 303, 5105, 30, 0);
INSERT INTO seat_reservation (valid_until, is_paid, movie_id, seat_id, client_id, total_price, is_deleted)
    VALUES (STR_TO_DATE('07/06/2020 8:00:00 PM', '%e/%c/%Y %r'), 1, 16, 304, 5106, 30, 0);