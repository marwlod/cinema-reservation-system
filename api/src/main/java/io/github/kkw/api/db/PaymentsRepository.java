package io.github.kkw.api.db;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Repository
public class PaymentsRepository {
    private final EntityManager entityManager;

    public PaymentsRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Transactional
    public boolean isAlreadyPaidSeat(int clientId, int reservationId) {
        final BigInteger isPaid = (BigInteger) entityManager
                .createNativeQuery("SELECT IF((" +
                        "SELECT COUNT(*) FROM seat_reservation WHERE client_id = ? AND seat_reservation_id = ? " +
                        "AND valid_until > ? AND is_paid = 1" +
                        ") > 0, TRUE, FALSE)")
                .setParameter(1, clientId)
                .setParameter(2, reservationId)
                .setParameter(3, Instant.now())
                .getSingleResult();
        return isPaid.intValue() == 1;
    }

    @Transactional
    public boolean isAlreadyPaidAdvanceHall(int clientId, int reservationId) {
        final BigInteger isPaidAdvance = (BigInteger) entityManager
                .createNativeQuery("SELECT IF((" +
                        "SELECT COUNT(*) FROM hall_reservation WHERE client_id = ? AND hall_reservation_id = ? " +
                        "AND valid_until > ? AND is_paid_advance = 1" +
                        ") > 0, TRUE, FALSE)")
                .setParameter(1, clientId)
                .setParameter(2, reservationId)
                .setParameter(3, Instant.now())
                .getSingleResult();
        return isPaidAdvance.intValue() == 1;
    }

    @Transactional
    public boolean isAlreadyPaidHall(int clientId, int reservationId) {
        final BigInteger isPaid = (BigInteger) entityManager
                .createNativeQuery("SELECT IF((" +
                        "SELECT COUNT(*) FROM hall_reservation WHERE client_id = ? AND hall_reservation_id = ? " +
                        "AND valid_until > ? AND is_paid_total = 1" +
                        ") > 0, TRUE, FALSE)")
                .setParameter(1, clientId)
                .setParameter(2, reservationId)
                .setParameter(3, Instant.now())
                .getSingleResult();
        return isPaid.intValue() == 1;
    }

    @Transactional
    public double getPaymentAmountSeat(int clientId, int reservationId) {
        final BigDecimal basePrice = (BigDecimal) entityManager
                .createNativeQuery("SELECT base_price FROM seat_reservation " +
                        "INNER JOIN movie ON seat_reservation.movie_id = movie.movie_id " +
                        "WHERE client_id = ? AND seat_reservation_id = ? AND valid_until > ?")
                .setParameter(1, clientId)
                .setParameter(2, reservationId)
                .setParameter(3, Instant.now())
                .getSingleResult();
        final BigInteger isVip = (BigInteger) entityManager
                .createNativeQuery("SELECT IF((" +
                        "SELECT COUNT(*) FROM seat_reservation " +
                        "INNER JOIN seat ON seat_reservation.seat_id = seat.seat_id " +
                        "WHERE client_id = ? AND seat_reservation_id = ? " +
                        "AND valid_until > ? AND is_vip = 1" +
                        ") > 0, TRUE, FALSE)")
                .setParameter(1, clientId)
                .setParameter(2, reservationId)
                .setParameter(3, Instant.now())
                .getSingleResult();
        return isVip.intValue() == 1 ? 2 * basePrice.doubleValue() : basePrice.doubleValue(); // twice the price for VIP
    }

    @Transactional
    public double getPaymentAmountAdvanceHall(int clientId, int reservationId) {
        return ((BigDecimal) entityManager
                .createNativeQuery("SELECT advance_price FROM hall_reservation " +
                        "INNER JOIN hall ON hall_reservation.hall_id = hall.hall_id " +
                        "WHERE client_id = ? AND hall_reservation_id = ? AND valid_until > ?")
                .setParameter(1, clientId)
                .setParameter(2, reservationId)
                .setParameter(3, Instant.now())
                .getSingleResult()).doubleValue();
    }

    @Transactional
    public double getPaymentAmountHall(int clientId, int reservationId) {
        return ((BigDecimal) entityManager
                .createNativeQuery("SELECT total_price FROM hall_reservation " +
                        "INNER JOIN hall ON hall_reservation.hall_id = hall.hall_id " +
                        "WHERE client_id = ? AND hall_reservation_id = ? AND valid_until > ?")
                .setParameter(1, clientId)
                .setParameter(2, reservationId)
                .setParameter(3, Instant.now())
                .getSingleResult()).doubleValue();
    }

    @Transactional
    public void finishPaymentSeat(int clientId, int reservationId) {
        entityManager
                .createNativeQuery("UPDATE seat_reservation SET is_paid = 1 " +
                        "WHERE client_id = ? AND seat_reservation_id = ?")
                .setParameter(1, clientId)
                .setParameter(2, reservationId)
                .executeUpdate();

        final Timestamp timeMovieEnds = (Timestamp) entityManager
                .createNativeQuery("SELECT end_date FROM seat_reservation " +
                        "INNER JOIN movie ON seat_reservation.movie_id = movie.movie_id " +
                        "WHERE seat_reservation_id = ?")
                .setParameter(1, reservationId)
                .getSingleResult();

        entityManager
                .createNativeQuery("UPDATE seat_reservation SET valid_until = ? " +
                        "WHERE client_id = ? AND seat_reservation_id = ?")
                .setParameter(1, timeMovieEnds)
                .setParameter(2, clientId)
                .setParameter(3, reservationId)
                .executeUpdate();
    }

    @Transactional
    public void finishAdvancePaymentHall(int clientId, int reservationId) {
        entityManager
                .createNativeQuery("UPDATE hall_reservation SET is_paid_advance = 1 " +
                        "WHERE client_id = ? AND hall_reservation_id = ?")
                .setParameter(1, clientId)
                .setParameter(2, reservationId)
                .executeUpdate();

        // TODO set proper amount of time
        entityManager
                .createNativeQuery("UPDATE hall_reservation SET valid_until = ? " +
                        "WHERE client_id = ? AND hall_reservation_id = ?")
                .setParameter(1, Instant.now().plus(1, ChronoUnit.DAYS))
                .setParameter(2, clientId)
                .setParameter(3, reservationId)
                .executeUpdate();
    }

    @Transactional
    public void finishPaymentHall(int clientId, int reservationId) {
        entityManager
                .createNativeQuery("UPDATE hall_reservation SET is_paid_total = 1 " +
                        "WHERE client_id = ? AND hall_reservation_id = ?")
                .setParameter(1, clientId)
                .setParameter(2, reservationId)
                .executeUpdate();

        final Date reservationDate = (Date) entityManager
                .createNativeQuery("SELECT reservation_date FROM hall_reservation " +
                        "WHERE client_id = ? AND hall_reservation_id = ?")
                .setParameter(1, clientId)
                .setParameter(2, reservationId)
                .getSingleResult();

        entityManager
                .createNativeQuery("UPDATE hall_reservation SET valid_until = ? " +
                        "WHERE client_id = ? AND hall_reservation_id = ?")
                .setParameter(1, Instant.ofEpochMilli(reservationDate.getTime()).plus(1, ChronoUnit.DAYS))
                .setParameter(2, clientId)
                .setParameter(3, reservationId)
                .executeUpdate();
    }
}
