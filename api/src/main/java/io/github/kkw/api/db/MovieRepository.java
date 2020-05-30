package io.github.kkw.api.db;

import io.github.kkw.api.db.dto.MovieEntity;
import io.github.kkw.api.model.Movie;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MovieRepository {
    private final EntityManager entityManager;

    public MovieRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public List<MovieEntity> showProgramme(Instant from, Instant to){
        return (List<MovieEntity>) entityManager
                .createNativeQuery("SELECT * FROM movie WHERE start_date>=? AND end_date<=?", MovieEntity.class)
                .setParameter(1, from)
                .setParameter(2, to)
                .getResultList();
    }

    @Transactional
    public boolean isHallIdValid(int hallId){
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
                        "WHERE ((start_date <= ? AND end_date > ?) AND hall_id = ?)"+
                        " OR ((start_date < ? AND end_date >= ?) AND hall_id = ?)"+
                        ") > 0, TRUE, FALSE)")
                .setParameter(1, startDate)
                .setParameter(2, startDate)
                .setParameter(3, hallId)
                .setParameter(4, endDate)
                .setParameter(5, endDate)
                .setParameter(6, hallId)
                .getSingleResult();
        return isConflict.intValue() == 1;
    }

    @Transactional
    public void addMovie(String name, Instant startDate, Instant endDate, double basePrice, int hallId){
        entityManager.createNativeQuery("INSERT INTO movie(name, start_date, end_date, base_price, hall_id)"+
                "VALUES (?,?,?,?,?)")
                .setParameter(1, name)
                .setParameter(2, startDate)
                .setParameter(3, endDate)
                .setParameter(4, basePrice)
                .setParameter(5, hallId)
                .executeUpdate();
    }

    @Transactional
    public int getMovies(Instant from, Instant to){
        BigInteger movies = (BigInteger) entityManager
                .createNativeQuery("SELECT COUNT(*) from movie " +
                        "WHERE start_date>=? AND end_date<=?")
                .setParameter(1, Timestamp.from(from))
                .setParameter(2, Timestamp.from(to))
                .getSingleResult();
        return movies.intValue();
    }

    @Transactional
    public boolean isMovieExists(String name){
        final BigInteger isMovieExists = (BigInteger) entityManager
                .createNativeQuery("SELECT IF((SELECT COUNT(*) FROM movie WHERE name = ?) > 0, TRUE, FALSE)")
                .setParameter(1,name)
                .getSingleResult();
        return isMovieExists.intValue() == 1;
    }

    @Transactional
    public int getMovieShowsCount(String name){
        final BigInteger movieShowsCounter = (BigInteger) entityManager
                .createNativeQuery("SELECT COUNT(*) FROM movie WHERE name = ?")
                .setParameter(1,name)
                .getSingleResult();
        return movieShowsCounter.intValue();
    }

    @Transactional
    public Instant getFirstShowingDateOfMovie(String name){
        final Timestamp movieShowsFromStartDate = (Timestamp) entityManager
                .createNativeQuery("SELECT MIN(start_date) FROM movie WHERE name = ?")
                .setParameter(1,name)
                .getSingleResult();
        return movieShowsFromStartDate.toInstant();
    }

    @Transactional
    public Instant getLastShowingDateOfMovie(String name){
        final Timestamp movieShowsToStartDate = (Timestamp) entityManager
                .createNativeQuery("SELECT MAX(start_date) FROM movie WHERE name = ?")
                .setParameter(1,name)
                .getSingleResult();
        return movieShowsToStartDate.toInstant();
    }

}
