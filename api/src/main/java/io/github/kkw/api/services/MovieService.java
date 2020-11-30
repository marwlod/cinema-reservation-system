package io.github.kkw.api.services;

import io.github.kkw.api.exceptions.HallNotFoundException;
import io.github.kkw.api.exceptions.MovieConflictException;
import io.github.kkw.api.exceptions.MovieDateException;
import io.github.kkw.api.exceptions.MoviesNotFoundException;
import io.github.kkw.api.model.Movie;

import java.time.Instant;
import java.util.List;

public interface MovieService {
    /**
     *
     * @param from date from which to show programme (beginning of chosen time frame)
     * @param to date to which to show programme (end of chosen time frame)
     * @return list of movies scheduled in chosen time frame
     * @throws MoviesNotFoundException when no movies were found in this period
     */
    List<Movie> showProgramme(final Instant from, final Instant to) throws MoviesNotFoundException;

    /**
     *
     * @param name name of the movie
     * @param startDate start date of the movie screening
     * @param endDate end date of the movie screening
     * @param basePrice price for a regular ticket
     * @param hallId ID of hall where the screening will take place
     * @param description description of the movie
     * @param link hyperlink to the movie image
     * @throws MovieDateException when there is a problem with given dates (e.g. end date before start date)
     * @throws HallNotFoundException when hall with given ID was not found
     * @throws MovieConflictException when there is already a screening scheduled in chosen hall for chosen time period
     */
    void addMovie(final String name,
                  final Instant startDate,
                  final Instant endDate,
                  final double basePrice,
                  final int hallId,
                  final String description,
                  final String link) throws MovieDateException, HallNotFoundException, MovieConflictException;
}
