package io.github.kkw.api.services;

import io.github.kkw.api.exceptions.FromAfterToDateException;
import io.github.kkw.api.exceptions.FutureDatesException;
import io.github.kkw.api.exceptions.HallNoReservationsException;
import io.github.kkw.api.exceptions.HallNotFoundException;
import io.github.kkw.api.exceptions.MovieNotFoundException;
import io.github.kkw.api.exceptions.MovieShowsNotFoundException;
import io.github.kkw.api.model.HallStatistics;
import io.github.kkw.api.model.MovieStatistics;
import io.github.kkw.api.model.Statistics;

import java.time.Instant;

public interface StatisticsService {

    /**
     *
     * @param from date from which to show statistics
     * @param to date to which to show statistics
     * @return general statistics in given time frame
     * @throws FutureDatesException when future dates were given
     * @throws FromAfterToDateException when "from" date is after "to" date
     */
    Statistics showStatistics(final Instant from, final Instant to) throws FutureDatesException, FromAfterToDateException;

    /**
     *
     * @param movieName name of the movie to show statistics for
     * @return statistics for a chosen movie
     * @throws MovieNotFoundException when movie with this name was not found
     * @throws MovieShowsNotFoundException when no screenings of this movie were found
     */
    MovieStatistics showStatisticsForMovie(String movieName) throws MovieNotFoundException, MovieShowsNotFoundException;

    /**
     *
     * @param hallId ID of hall to show statistics for
     * @return statistics for chosen hall
     * @throws HallNotFoundException when hall with given ID was not found
     * @throws HallNoReservationsException when there were no reservations for given hall
     */
    HallStatistics showStatisticsForHall(int hallId) throws HallNotFoundException, HallNoReservationsException;
}
