package io.github.kkw.api.db;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Repository
public class HallReservationRepository {
    private final EntityManager entityManager;

    public HallReservationRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public boolean isReserved(int hallId, Date date) {
        final BigInteger isReserved = (BigInteger) entityManager
                .createNativeQuery("SELECT IF((" +
                        "SELECT COUNT(*) FROM hall_reservation WHERE hall_id = ? AND reservation_date = ? AND valid_until > ?" +
                        ") > 0, TRUE, FALSE)")
                .setParameter(1, hallId)
                .setParameter(2, new java.sql.Date(date.getTime()))
                .setParameter(3, Instant.now())
                .getSingleResult();
        return isReserved.intValue() == 1;
    }

    @Transactional
    public void createHallReservation(int clientId, int hallId, Date date) {
        entityManager
                .createNativeQuery("INSERT INTO hall_reservation (valid_until, is_paid_advance, is_paid_total, reservation_date, hall_id, client_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?)")
                .setParameter(1, validFor1HourButMaxUntilDayBefore(date))
                .setParameter(2, 0) // not paid advance
                .setParameter(3, 0) // not paid total
                .setParameter(4, date)
                .setParameter(5, hallId)
                .setParameter(6, clientId)
                .executeUpdate();
    }

    private Instant validFor1HourButMaxUntilDayBefore(Date date) {
        long secondsFromNowToDayBeforeDate = Instant.now().until(date.toInstant().minus(1, ChronoUnit.DAYS), ChronoUnit.SECONDS);
        long secondsInHour = 3600;
        return Instant.now().plus(Math.min(secondsFromNowToDayBeforeDate, secondsInHour), ChronoUnit.SECONDS);
    }

    @Transactional
    public int getHallReservationId(int clientId, int hallId, Date date) {
        return (int) entityManager
                .createNativeQuery("SELECT hall_reservation_id FROM hall_reservation " +
                        "WHERE client_id = ? AND hall_id = ? AND reservation_date = ? AND valid_until > ?")
                .setParameter(1, clientId)
                .setParameter(2, hallId)
                .setParameter(3, new java.sql.Date(date.getTime()))
                .setParameter(4, Instant.now())
                .getSingleResult();
    }

    @Transactional
    public boolean reservationDoesntExist(int clientId, int reservationId) {
        final BigInteger isReserved = (BigInteger) entityManager
                .createNativeQuery("SELECT IF((" +
                        "SELECT COUNT(*) FROM hall_reservation WHERE client_id = ? AND hall_reservation_id = ? AND valid_until > ?" +
                        ") > 0, TRUE, FALSE)")
                .setParameter(1, clientId)
                .setParameter(2, reservationId)
                .setParameter(3, Instant.now())
                .getSingleResult();
        return isReserved.intValue() == 0;
    }

    @Transactional
    public void deleteHallReservation(int clientId, int reservationId) {
        entityManager
                .createNativeQuery("UPDATE hall_reservation SET valid_until = ? WHERE client_id = ? AND hall_reservation_id = ? AND valid_until > ?")
                .setParameter(1, Instant.now())
                .setParameter(2, clientId)
                .setParameter(3, reservationId)
                .setParameter(4, Instant.now())
                .executeUpdate();
    }
}
