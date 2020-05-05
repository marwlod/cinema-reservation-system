package io.github.kkw.api.db;

import io.github.kkw.api.model.ReservationId;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;

@Repository
public class SeatReservationRepository {
    private final EntityManager entityManager;

    public SeatReservationRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public boolean movieDoesntExist(int movieId) {
        final BigInteger movieExists = (BigInteger) entityManager
                .createNativeQuery("SELECT IF((SELECT COUNT(*) FROM movie WHERE movie_id = ?) > 0, TRUE, FALSE)")
                .setParameter(1, movieId)
                .getSingleResult();
        return movieExists.intValue() == 0;
    }

    @Transactional
    public boolean seatForMovieDoesntExist(int movieId, int seatId) {
        final BigInteger seatForMovieExists = (BigInteger) entityManager
                .createNativeQuery("SELECT IF((" +
                        "SELECT COUNT(*) FROM movie " +
                        "INNER JOIN hall ON movie.hall_id = hall.hall_id " +
                        "INNER JOIN seat ON seat.hall_id = hall.hall_id " +
                        "WHERE movie_id = ? AND seat_id = ?) " +
                        "> 0, TRUE, FALSE)")
                .setParameter(1, movieId)
                .setParameter(2, seatId)
                .getSingleResult();
        return seatForMovieExists.intValue() == 0;
    }

    @Transactional
    public boolean isReserved(int movieId, int seatId) {
        final BigInteger isReserved = (BigInteger) entityManager
                .createNativeQuery("SELECT IF((" +
                        "SELECT COUNT(*) FROM seat_reservation WHERE movie_id = ? AND seat_id = ? AND valid_until > ?" +
                        ") > 0, TRUE, FALSE)")
                .setParameter(1, movieId)
                .setParameter(2, seatId)
                .setParameter(3, Instant.now())
                .getSingleResult();
        return isReserved.intValue() == 1;
    }

    @Transactional
    public void createSeatReservation(int clientId, int movieId, int seatId) {
        final Timestamp timeMovieEnds = (Timestamp) entityManager
                .createNativeQuery("SELECT end_date FROM movie " +
                        "WHERE movie_id=?")
                .setParameter(1, movieId)
                .getSingleResult();

        entityManager
                .createNativeQuery("INSERT INTO seat_reservation (valid_until, is_paid, movie_id, seat_id, client_id) " +
                        "VALUES (?, ?, ?, ?, ?)")
                .setParameter(1, timeMovieEnds)
                .setParameter(2, 0) // not paid
                .setParameter(3, movieId)
                .setParameter(4, seatId)
                .setParameter(5, clientId)
                .executeUpdate();
    }

    @Transactional
    public int getSeatReservationId(int clientId, int movieId, int seatId) {
        return (int) entityManager
                .createNativeQuery("SELECT seat_reservation_id FROM seat_reservation " +
                        "WHERE client_id = ? AND movie_id = ? AND seat_id = ? AND valid_until > ?")
                .setParameter(1, clientId)
                .setParameter(2, movieId)
                .setParameter(3, seatId)
                .setParameter(4, Instant.now())
                .getSingleResult();
    }

    @Transactional
    public boolean reservationDoesntExist(int clientId, int reservationId) {
        final BigInteger isReserved = (BigInteger) entityManager
                .createNativeQuery("SELECT IF((" +
                        "SELECT COUNT(*) FROM seat_reservation WHERE client_id = ? AND seat_reservation_id = ? AND valid_until > ?" +
                        ") > 0, TRUE, FALSE)")
                .setParameter(1, clientId)
                .setParameter(2, reservationId)
                .setParameter(3, Instant.now())
                .getSingleResult();
        return isReserved.intValue() == 0;
    }

    @Transactional
    public void deleteSeatReservation(int clientId, int reservationId) {
        entityManager
                .createNativeQuery("UPDATE seat_reservation SET valid_until = ? WHERE client_id = ? AND seat_reservation_id = ? AND valid_until > ?")
                .setParameter(1, Instant.now())
                .setParameter(2, clientId)
                .setParameter(3, reservationId)
                .setParameter(4, Instant.now())
                .executeUpdate();
    }
}
