package io.github.kkw.api.services;

import io.github.kkw.api.db.SeatReservationRepository;
import io.github.kkw.api.exceptions.MovieNotFoundException;
import io.github.kkw.api.exceptions.ReservationNotFoundException;
import io.github.kkw.api.exceptions.SeatNotFoundException;
import io.github.kkw.api.exceptions.SeatReservedException;
import io.github.kkw.api.model.ClientId;
import io.github.kkw.api.model.ReservationId;
import org.springframework.stereotype.Service;

@Service
public class SeatReservationService {
    private final SeatReservationRepository seatReservationRepository;

    public SeatReservationService(SeatReservationRepository seatReservationRepository) {
        this.seatReservationRepository = seatReservationRepository;
    }

    public ReservationId createSeatReservation(ClientId clientId, int movieId, int seatId)
            throws MovieNotFoundException, SeatNotFoundException, SeatReservedException {
        if (seatReservationRepository.movieDoesntExist(movieId)) {
            throw new MovieNotFoundException("Movie with this ID not found");
        }
        if (seatReservationRepository.seatForMovieDoesntExist(movieId, seatId)) {
            throw new SeatNotFoundException("Seat with this ID for this movie not found");
        }
        if (seatReservationRepository.isReserved(movieId, seatId)) {
            throw new SeatReservedException("Chosen seat for this movie is already reserved");
        }
        seatReservationRepository.createSeatReservation(clientId.getId(), movieId, seatId);
        return new ReservationId(seatReservationRepository.getSeatReservationId(clientId.getId(), movieId, seatId));
    }

    public void deleteSeatReservation(ClientId clientId, ReservationId reservationId)
            throws ReservationNotFoundException {
        if (seatReservationRepository.reservationDoesntExist(clientId.getId(), reservationId.getId())) {
            throw new ReservationNotFoundException("Reservation with this ID not found");
        }
        seatReservationRepository.deleteSeatReservation(clientId.getId(), reservationId.getId());
    }
}
