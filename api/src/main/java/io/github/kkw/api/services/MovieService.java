package io.github.kkw.api.services;

import io.github.kkw.api.db.dto.MovieEntity;
import io.github.kkw.api.db.MovieRepository;
import io.github.kkw.api.exceptions.MovieConflictException;
import io.github.kkw.api.exceptions.MovieDateException;
import io.github.kkw.api.exceptions.HallNotFoundException;
import io.github.kkw.api.exceptions.MoviesNotFoundException;
import io.github.kkw.api.model.Movie;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> showProgramme(final Instant from, final Instant to) throws MoviesNotFoundException {
        List<MovieEntity> movieEntities = movieRepository.showProgramme(from, to);
        if(movieEntities.isEmpty()){
            throw new MoviesNotFoundException("Cannot find any movies in this period");
        }
        return movieEntities.stream().map(Movie::new).collect(Collectors.toList());
    }

    private boolean areDatesValid(final Instant startDate, final Instant endDate){
        Instant minPossiblyDate = Instant.now();
        if (minPossiblyDate.compareTo(startDate) > 0) return false;
        return endDate.compareTo(startDate) > 0;
    }

    public void addMovie(final String name,
                         final Instant startDate,
                         final Instant endDate,
                         final double basePrice,
                         final int hallId) throws MovieDateException, HallNotFoundException, MovieConflictException {
        if(!areDatesValid(startDate, endDate)){
            throw new MovieDateException("End date must be later than start date. Both must be in the future");
        }
        if(!movieRepository.isHallIdValid(hallId)){
            throw new HallNotFoundException("Hall with this ID not found");
        }
        if(movieRepository.isMovieConflict(startDate, endDate, hallId)){
            throw new MovieConflictException("There is a conflict with another movie " +
                    "(another movie is scheduled in that time in chosen hall)");
        }
        movieRepository.addMovie(name,startDate,endDate,basePrice,hallId);
    }
}
