package io.github.kkw.api.services;

import io.github.kkw.api.db.SeatReservationRepository;
import io.github.kkw.api.db.dto.SeatEntity;
import io.github.kkw.api.exceptions.MovieNotFoundException;
import io.github.kkw.api.exceptions.NoFreeSeatsException;
import io.github.kkw.api.exceptions.ReservationNotFoundException;
import io.github.kkw.api.exceptions.SeatNotFoundException;
import io.github.kkw.api.exceptions.SeatReservedException;
import io.github.kkw.api.model.ClientId;
import io.github.kkw.api.model.ReservationId;
import io.github.kkw.api.model.Seat;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SeatReservationService {
    private final SeatReservationRepository seatReservationRepository;

    public SeatReservationService(SeatReservationRepository seatReservationRepository) {
        this.seatReservationRepository = seatReservationRepository;
    }

    public ReservationId createSeatReservation(ClientId clientId, int movieId, int seatId, Optional<String> code)
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
        seatReservationRepository.createSeatReservation(clientId.getClientId(), movieId, seatId, code.isEmpty() ? null : code.get());
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

    public List<Seat> showAllSeats(int movieId) throws MovieNotFoundException {
        if (seatReservationRepository.movieDoesntExist(movieId)) {
            throw new MovieNotFoundException("Movie with this ID not found");
        }
        List<SeatEntity> allSeats = seatReservationRepository.getAllSeats(movieId);
        return allSeats.stream().map(Seat::new).collect(Collectors.toList());
    }
}
