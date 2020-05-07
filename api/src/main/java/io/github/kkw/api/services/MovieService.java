package io.github.kkw.api.services;

import io.github.kkw.api.db.MovieRepository;
import io.github.kkw.api.exceptions.*;
import io.github.kkw.api.model.Movie;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class MovieService {

    private MovieRepository movieRepository;
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }



    public List<Movie> showProgramme() throws MoviesNotFoundException {
        if(!movieRepository.anyMovieExistsInPeriod()){
            throw new MoviesNotFoundException("Cannot find any movies in this period");
        }
        return movieRepository.showProgramme();
    }


    private boolean checkMovieStartDate(Instant startDate, Instant endDate){
        Instant minPossiblyDate = Instant.now();
        Instant maxPossiblyDate = Instant.now().plus(14, ChronoUnit.DAYS);
        if(minPossiblyDate.compareTo(startDate)>1
        || endDate.compareTo(maxPossiblyDate)>1) return false;
        if(startDate.compareTo(endDate)>1) return false;
        return true;
    }


    private boolean ifMissingMovieInsertValues(String name, Instant startDate,
                                               Instant endDate, double basePrice,
                                               int hallId){
        if(name==null || startDate==null || endDate==null) return true;
        return false;
    }

    public void addMovie(String name, Instant startDate, Instant endDate, double basePrice, int hallId) throws MovieDateException, MovieHallIdException, MovieConflictException, MovieMissingValuesException {
        if(ifMissingMovieInsertValues(name,startDate,endDate,basePrice,hallId)){
            throw new MovieMissingValuesException("Missing movie values");
        }
        if(!checkMovieStartDate(startDate, endDate)){
            throw new MovieDateException("Start date cannot be later than 14 days and " +
                    "end date cannot be from the past. End date must be later than " +
                    "start date.");
        }
        if(!movieRepository.checkMovieHallId(hallId)){
            throw new MovieHallIdException("Wrong movie hall number");
        }
        if(movieRepository.isMovieConflict(startDate,endDate,hallId)){
            throw new MovieConflictException("There is a conflict with another movie");
        }
        movieRepository.addMovie(name,startDate,endDate,basePrice,hallId);
    }
}
