package io.github.kkw.api.db;

import io.github.kkw.api.db.dto.HallEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Repository
public class HallRepository {

    private final EntityManager entityManager;

    public HallRepository(EntityManager entityManager){
        this.entityManager=entityManager;
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public List<HallEntity> showCinemaHalls(){
        entityManager
                .createNativeQuery("SELECT hall_id, advance_price, total_price," +
                        "screen_size FROM hall")
                .getResultList();
        return (List<HallEntity>) entityManager;
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public List<HallEntity> showAvailableCinemaHalls(Instant date){
        return (List<HallEntity>) entityManager
                .createNativeQuery("SELECT hall_id, advance_price, total_price," +
                        "screen_size FROM hall " +
                        "INNER JOIN hall_reservation ON hall.hall_id = hall_reservation.hall_id " +
                        "WHERE NOT EXISTS ( SELECT * FROM hall_reservation WHERE reservation_date=?" +
                        "OR (reservation_date=? AND valid_until<>)")
                .setParameter(1,date)
                .setParameter(2,date)
                .setParameter(3,Instant.now().plus(14, ChronoUnit.DAYS))
                .getResultList();
    }

}
