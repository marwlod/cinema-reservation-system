package io.github.kkw.api.services;

import io.github.kkw.api.exceptions.CinemaHallsNotFoundException;
import io.github.kkw.api.exceptions.DateTooSoonException;
import io.github.kkw.api.exceptions.HallNotFoundException;
import io.github.kkw.api.exceptions.HallReservedException;
import io.github.kkw.api.exceptions.ReservationNotFoundException;
import io.github.kkw.api.model.ClientId;
import io.github.kkw.api.model.Hall;
import io.github.kkw.api.model.HallReservation;
import io.github.kkw.api.model.ReservationId;

import java.time.Instant;
import java.util.List;

public interface HallReservationService {
    /**
     *
     * @param clientId ID of the calling client
     * @param hallId ID of the hall to reserve
     * @param date date to reserve the hall on
     * @return reservation ID of newly created hall reservation
     * @throws HallReservedException when hall is already reserved
     * @throws HallNotFoundException when hall is not found
     * @throws DateTooSoonException reservations for halls can only be done 2 weeks in advance
     */
    ReservationId createHallReservation(ClientId clientId, int hallId, Instant date) throws HallReservedException, HallNotFoundException, DateTooSoonException;

    /**
     *
     * @param clientId ID of the calling client
     * @param reservationId ID of the reservation to delete
     * @throws ReservationNotFoundException when reservation with this ID was not found
     */
    void deleteHallReservation(ClientId clientId, ReservationId reservationId) throws ReservationNotFoundException;

    /**
     *
     * @param date date to show available halls for
     * @return list of all halls that are available to reserve on chosen date
     * @throws CinemaHallsNotFoundException when no available cinema halls were found
     * @throws DateTooSoonException reservations for halls can only be done 2 weeks in advance
     */
    List<Hall> showAvailableCinemaHalls(final Instant date) throws CinemaHallsNotFoundException, DateTooSoonException;

    /**
     *
     * @param clientId ID of the calling client
     * @param from date to show reservations from
     * @param to date to show reservation to
     * @return list of all hall reservations for chosen client in chosen time frame
     */
    List<HallReservation> showReservations(ClientId clientId, Instant from, Instant to);
}
