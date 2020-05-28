package io.github.kkw.api.db;

import io.github.kkw.api.db.dto.SeatEntity;
import io.github.kkw.api.db.dto.SpecialOfferEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private BigDecimal calculateTotalPrice(BigDecimal basePrice, int discountPercentage, boolean isVipRecord){
        int isVip = 0;
        if(isVipRecord) isVip=1;
        BigDecimal discountPartOfBase = new BigDecimal(1-discountPercentage*0.01);
        BigDecimal priceWithDiscount = basePrice.multiply(new BigDecimal(1-discountPercentage*0.01));
        BigDecimal vipPrice = basePrice.multiply(new BigDecimal(0.5*isVip));
        BigDecimal totalPrice = priceWithDiscount.add(vipPrice);
        return totalPrice;
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public void createSeatReservation(int clientId, int movieId, int seatId, String code) {
         BigDecimal basePrice = (BigDecimal) entityManager
                .createNativeQuery("SELECT base_price FROM movie WHERE movie_id=?")
                .setParameter(1,movieId)
                .getSingleResult();
        final List<SpecialOfferEntity> specialOfferRecord = (List<SpecialOfferEntity>) entityManager
                .createNativeQuery("SELECT special_offer_id, code, percentage " +
                        "FROM special_offers WHERE code=?", SpecialOfferEntity.class)
                .setParameter(1,code)
                .getResultList();
        int discountPercentage = 0;
        if(!specialOfferRecord.isEmpty()) discountPercentage = specialOfferRecord.get(0).getPercentage();
        final Boolean isVipRecord = (Boolean) entityManager
                .createNativeQuery("SELECT is_vip FROM seat WHERE seat_id=?")
                .setParameter(1,seatId)
                .getSingleResult();
        double totalPrice = calculateTotalPrice(basePrice,discountPercentage,isVipRecord).doubleValue();
        final Timestamp timeMovieEnds = (Timestamp) entityManager
                .createNativeQuery("SELECT end_date FROM movie " +
                        "WHERE movie_id=?")
                .setParameter(1, movieId)
                .getSingleResult();

        entityManager
                .createNativeQuery("INSERT INTO seat_reservation (valid_until, is_paid, movie_id, seat_id, client_id, total_price) " +
                        "VALUES (?, ?, ?, ?, ?, ?)")
                .setParameter(1, timeMovieEnds)
                .setParameter(2, 0) // not paid
                .setParameter(3, movieId)
                .setParameter(4, seatId)
                .setParameter(5, clientId)
                .setParameter(6,totalPrice)
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

    @Transactional
    @SuppressWarnings("unchecked")
    public List<SeatEntity> getFreeSeats(int movieId) {
        final List<SeatEntity> allSeatsForMovie = getAllSeats(movieId);

        final List<SeatEntity> reservedSeatsForMovie = (List<SeatEntity>) entityManager
                .createNativeQuery("SELECT seat.seat_id, movie.hall_id, row_no, seat_no, is_vip, base_price " +
                        "FROM movie " +
                        "INNER JOIN seat_reservation ON movie.movie_id = seat_reservation.movie_id " +
                        "INNER JOIN seat ON seat.seat_id = seat_reservation.seat_id " +
                        "WHERE movie.movie_id = ? AND valid_until > ?", SeatEntity.class)
                .setParameter(1, movieId)
                .setParameter(2, Instant.now())
                .getResultList();
        final Set<SeatEntity> reserved = new HashSet<>(reservedSeatsForMovie);
        final List<SeatEntity> res = new ArrayList<>();
        for (final SeatEntity seat : allSeatsForMovie) {
            if (!reserved.contains(seat)) {
                res.add(seat);
            }
        }
        return res;
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public List<SeatEntity> getAllSeats(int movieId) {
        return (List<SeatEntity>) entityManager
                .createNativeQuery("SELECT seat.seat_id, hall.hall_id, row_no, seat_no, is_vip, base_price " +
                        "FROM movie " +
                        "INNER JOIN hall ON hall.hall_id = movie.hall_id " +
                        "INNER JOIN seat ON hall.hall_id = seat.hall_id " +
                        "WHERE movie_id = ?", SeatEntity.class)
                .setParameter(1, movieId)
                .getResultList();
    }

    @Transactional
    public int getSeatReservations(Instant from, Instant to){
        final BigInteger result = (BigInteger) entityManager
                .createNativeQuery("SELECT COUNT(*) FROM seat_reservation WHERE valid_until>=? AND valid_until<=?")
                .setParameter(1, Timestamp.from(from))
                .setParameter(2, Timestamp.from(to))
                .getSingleResult();
        return result.intValue();
    }

    @Transactional
    public boolean ifClientReservedAnySeat(int clientId){
        final BigInteger isReserved = (BigInteger) entityManager
                .createNativeQuery("SELECT IF((" +
                        "SELECT COUNT(*) FROM seat_reservation WHERE client_id = ?" +
                        ") > 0, TRUE, FALSE)")
                .setParameter(1, clientId)
                .getSingleResult();
        return isReserved.intValue() == 0;
    }

    @Transactional
    public double getMoneyEarned(Instant from, Instant to){
        BigDecimal moneyEarned = (BigDecimal) entityManager
                .createNativeQuery("SELECT SUM(total_price) FROM seat_reservation WHERE valid_until>=? AND valid_until<=?")
                .setParameter(1, Timestamp.from(from))
                .setParameter(2, Timestamp.from(to))
                .getSingleResult();
        if(moneyEarned==null) return 0.00;
        return moneyEarned.doubleValue();
    }

}
