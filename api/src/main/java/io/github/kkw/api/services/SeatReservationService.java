package io.github.kkw.api.services;

import io.github.kkw.api.exceptions.MovieNotFoundException;
import io.github.kkw.api.exceptions.NoFreeSeatsException;
import io.github.kkw.api.exceptions.ReservationNotFoundException;
import io.github.kkw.api.exceptions.SeatNotFoundException;
import io.github.kkw.api.exceptions.SeatReservedException;
import io.github.kkw.api.model.ClientId;
import io.github.kkw.api.model.ReservationId;
import io.github.kkw.api.model.Seat;
import io.github.kkw.api.model.SeatReservation;

import java.time.Instant;
import java.util.List;

public interface SeatReservationService {

    /**
     *
     * @param clientId ID of calling client
     * @param movieId ID of movie to create reservation for
     * @param seatId ID of chosen seat to reserve
     * @return ID of newly created reservation
     * @throws MovieNotFoundException when movie with given ID was not found
     * @throws SeatNotFoundException when seat with given ID was not found
     * @throws SeatReservedException when seat with given ID was already reserved
     */
    ReservationId createSeatReservation(ClientId clientId, int movieId, int seatId)
            throws MovieNotFoundException, SeatNotFoundException, SeatReservedException;

    /**
     *
     * @param clientId ID of calling client
     * @param reservationId ID of seat reservation to delete
     * @throws ReservationNotFoundException when reservation with this ID was not found
     */
    void deleteSeatReservation(ClientId clientId, ReservationId reservationId)
     throws ReservationNotFoundException;

    /**
     *
     * @param movieId ID of movie to show free seats for
     * @return list of all available seats for chosen movie screening
     * @throws MovieNotFoundException when movie with given ID was not found
     * @throws NoFreeSeatsException when there are no free seats available for chosen movie
     */
    List<Seat> showFreeSeats(int movieId) throws MovieNotFoundException, NoFreeSeatsException;

    /**
     *
     * @param clientId ID of calling client
     * @param from date to show seat reservations from
     * @param to date to show seat reservations to
     * @return list of all seat reservation for given client in given time frame
     */
    List<SeatReservation> showReservations(ClientId clientId, Instant from, Instant to);
}
