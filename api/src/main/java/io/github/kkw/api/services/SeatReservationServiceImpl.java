package io.github.kkw.api.services;

import io.github.kkw.api.db.SeatReservationRepository;
import io.github.kkw.api.db.dto.SeatEntity;
import io.github.kkw.api.db.dto.SeatReservationEntity;
import io.github.kkw.api.exceptions.MovieNotFoundException;
import io.github.kkw.api.exceptions.NoFreeSeatsException;
import io.github.kkw.api.exceptions.ReservationNotFoundException;
import io.github.kkw.api.exceptions.SeatNotFoundException;
import io.github.kkw.api.exceptions.SeatReservedException;
import io.github.kkw.api.model.ClientId;
import io.github.kkw.api.model.ReservationId;
import io.github.kkw.api.model.Seat;
import io.github.kkw.api.model.SeatReservation;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SeatReservationServiceImpl implements SeatReservationService {
    private final SeatReservationRepository seatReservationRepository;

    public SeatReservationServiceImpl(SeatReservationRepository seatReservationRepository) {
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
        seatReservationRepository.createSeatReservation(clientId.getClientId(), movieId, seatId);
        return new ReservationId(seatReservationRepository.getSeatReservationId(clientId.getClientId(), movieId, seatId));
    }

    public void deleteSeatReservation(ClientId clientId, ReservationId reservationId)
            throws ReservationNotFoundException {
        if (seatReservationRepository.reservationDoesntExist(clientId.getClientId(), reservationId.getReservationId())) {
            throw new ReservationNotFoundException("Reservation with this ID not found");
        }
        seatReservationRepository.deleteSeatReservation(clientId.getClientId(), reservationId.getReservationId());
    }

    public List<Seat> showFreeSeats(int movieId) throws MovieNotFoundException, NoFreeSeatsException {
        if (seatReservationRepository.movieDoesntExist(movieId)) {
            throw new MovieNotFoundException("Movie with this ID not found");
        }
        List<SeatEntity> freeSeats = seatReservationRepository.getFreeSeats(movieId);
        if (freeSeats.isEmpty()) {
            throw new NoFreeSeatsException("No seats available to reserve for this movie");
        }
        return freeSeats.stream().map(Seat::new).collect(Collectors.toList());
    }

    public List<SeatReservation> showReservations(ClientId clientId, Instant from, Instant to) {
        List<SeatReservationEntity> allReservations = seatReservationRepository.getReservations(clientId.getClientId(), from, to);
        return allReservations.stream().map(SeatReservation::new).collect(Collectors.toList());
    }
}
