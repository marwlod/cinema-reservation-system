package io.github.kkw.api.db;

import io.github.kkw.api.db.dto.HallEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.sound.midi.Soundbank;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class HallReservationRepository {
    private final EntityManager entityManager;

    public HallReservationRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public boolean isReserved(int hallId, Instant date) {
        final BigInteger isReserved = (BigInteger) entityManager
                .createNativeQuery("SELECT IF((" +
                        "SELECT COUNT(*) FROM hall_reservation WHERE hall_id = ? AND reservation_date = ? AND valid_until > ?" +
                        ") > 0, TRUE, FALSE)")
                .setParameter(1, hallId)
                .setParameter(2, date)
                .setParameter(3, Instant.now())
                .getSingleResult();
        return isReserved.intValue() == 1;
    }

    @Transactional
    public void createHallReservation(int clientId, int hallId, Instant date) {
        entityManager
                .createNativeQuery("INSERT INTO hall_reservation (valid_until, is_paid_advance, is_paid_total, reservation_date, hall_id, client_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?)")
                //TODO set proper valid_until
                .setParameter(1, Instant.now().plus(1, ChronoUnit.HOURS))
                .setParameter(2, 0) // not paid advance
                .setParameter(3, 0) // not paid total
                .setParameter(4, date)
                .setParameter(5, hallId)
                .setParameter(6, clientId)
                .executeUpdate();
    }

    @Transactional
    public int getHallReservationId(int clientId, int hallId, Instant date) {
        return (int) entityManager
                .createNativeQuery("SELECT hall_reservation_id FROM hall_reservation " +
                        "WHERE client_id = ? AND hall_id = ? AND reservation_date = ? AND valid_until > ?")
                .setParameter(1, clientId)
                .setParameter(2, hallId)
                .setParameter(3, date)
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

    @Transactional
    @SuppressWarnings("unchecked")
    public List<HallEntity> showCinemaHalls(){
        return (List<HallEntity>) entityManager
                .createNativeQuery("SELECT hall.hall_id, advance_price, total_price,screen_size, regular_seats, vip_seats " +
                        "FROM hall " +
                        "LEFT JOIN (SELECT hall_id, COUNT(*) as regular_seats from seat WHERE is_vip = 0 GROUP BY hall_id) " +
                                "as reg ON reg.hall_id = hall.hall_id " +
                        "LEFT JOIN (SELECT hall_id, COUNT(*) as vip_seats from seat WHERE is_vip = 1 GROUP BY hall_id) " +
                                "as vip ON vip.hall_id = hall.hall_id",
                        HallEntity.class)
                .getResultList();
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public List<HallEntity> showAvailableCinemaHalls(Instant date){
        final List<HallEntity> allHalls = showCinemaHalls();
        final List<HallEntity> hallsReserved = (List<HallEntity>) entityManager
                .createNativeQuery("SELECT hall.hall_id, advance_price, total_price, screen_size " +
                        "FROM hall " +
                        "INNER JOIN hall_reservation ON hall.hall_id = hall_reservation.hall_id " +
                        "WHERE reservation_date = ? AND valid_until > ?", HallEntity.class)
                .setParameter(1, date)
                .setParameter(2, Instant.now())
                .getResultList();
        final Set<HallEntity> reserved = new HashSet<>(hallsReserved);
        final List<HallEntity> res = new ArrayList<>();
        for (final HallEntity hall : allHalls) {
            if (!reserved.contains(hall)) {
                res.add(hall);
            }
        }
        return res;
    }


    @Transactional
    public int getHallReservations(Instant from, Instant to){
        BigInteger hallReservations = (BigInteger) entityManager
                .createNativeQuery("SELECT COUNT(*) from hall_reservation WHERE valid_until>=? AND valid_until<=?")
                .setParameter(1, Timestamp.from(from))
                .setParameter(2, Timestamp.from(to))
                .getSingleResult();
        return hallReservations.intValue();
    }

    @Transactional
    public boolean ifClientReservedAnyHall(int clientId){
        final BigInteger isReserved = (BigInteger) entityManager
                .createNativeQuery("SELECT IF((" +
                        "SELECT COUNT(*) FROM hall_reservation WHERE client_id = ?" +
                        ") > 0, TRUE, FALSE)")
                .setParameter(1, clientId)
                .getSingleResult();
        return isReserved.intValue() == 0;
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public double getMoneyEarnedFromHalls(Instant from, Instant to){
        List<Integer> halls = (List<Integer>) entityManager
                .createNativeQuery("SELECT hall_id FROM hall_reservation WHERE valid_until>=? AND valid_until<=?")
                .setParameter(1,Timestamp.from(from))
                .setParameter(2, Timestamp.from(to))
                .getResultList();
        List<BigDecimal> hallsPrices = (List<BigDecimal>) entityManager
                .createNativeQuery("SELECT total_price FROM hall")
                .getResultList();
        if(halls.isEmpty() || hallsPrices.isEmpty()) return 0.00;
        BigDecimal sumHallPayments = BigDecimal.ZERO;
        for(int i=0; i<halls.size(); i++){
            sumHallPayments = sumHallPayments.add(hallsPrices.get(halls.get(i)-1));
        }
        System.out.println("getMoneyEarnedFromHalls: "+sumHallPayments.doubleValue());
        return sumHallPayments.doubleValue();
    }

    @Transactional
    public int halls(){
        BigInteger halls = (BigInteger) entityManager
                .createNativeQuery("SELECT COUNT(*) FROM hall")
                .getSingleResult();
        return halls.intValue();
    }
}
