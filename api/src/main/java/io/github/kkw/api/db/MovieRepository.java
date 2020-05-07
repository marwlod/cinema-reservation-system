package io.github.kkw.api.db;

import io.github.kkw.api.model.Movie;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Repository
public class MovieRepository {

    private final EntityManager entityManager;
    public MovieRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Transactional
    public boolean anyMovieExistsInPeriod(){
        final BigInteger ifMovieExists = (BigInteger) entityManager
                .createNativeQuery("SELECT IF((SELECT COUNT(*) FROM movie " +
                        "WHERE start_date >= ? AND end_date<=?) > 0, TRUE, FALSE)")
                .setParameter(1,Instant.now())
                .setParameter(2, Instant.now().plus(7,ChronoUnit.DAYS))
                .getSingleResult();
        return ifMovieExists.intValue() == 1;
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public List<Movie> showProgramme(){
        final List<Movie> movies = entityManager
                .createNativeQuery("SELECT movie_id FROM movie WHERE start_date>=? AND end_date<=?")
                .setParameter(1, Instant.now())
                .setParameter(2, Instant.now().plus(7, ChronoUnit.DAYS))
                .getResultList();
        return movies;
    }

    @Transactional
    public boolean checkMovieHallId(int hallId){
        final BigInteger isMovieHall = (BigInteger) entityManager
                .createNativeQuery("SELECT IF((SELECT COUNT(*) FROM hall WHERE hall_id = ?) > 0, TRUE, FALSE)")
                .setParameter(1,hallId)
                .getSingleResult();
        return isMovieHall.intValue() == 1;
    }

    @Transactional
    public boolean isMovieConflict(Instant startDate, Instant endDate, int hallId){
        final BigInteger isConflict = (BigInteger) entityManager
                .createNativeQuery("SELECT IF((SELECT COUNT(*) FROM movie "+
                        "WHERE ((start_date <= startDate AND end_date > startDate) AND hall_id = hallId)"+
                        " OR ((start_date < endDate AND end_date >= endDate) AND hall_id = hallId)"+
                        ") > 0, TRUE, FALSE)")
                .getSingleResult();
        return isConflict.intValue() == 1;
    }

    @Transactional
    public void addMovie(String name, Instant startDate, Instant endDate, double basePrice, int hallId){
        entityManager.createNativeQuery("INSERT INTO movie(movie_id, name, start_date, endDate, base_price, hall_id)"+
                "VALUES (NULL,?,?,?,?,?)")
                .setParameter(1, name)
                .setParameter(2, startDate)
                .setParameter(3, endDate)
                .setParameter(4, basePrice)
                .setParameter(5, hallId)
                .executeUpdate();
    }

}
