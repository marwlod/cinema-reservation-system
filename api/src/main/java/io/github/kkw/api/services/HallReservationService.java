package io.github.kkw.api.services;

import io.github.kkw.api.db.HallReservationRepository;
import io.github.kkw.api.db.MovieRepository;
import io.github.kkw.api.exceptions.HallReservedException;
import io.github.kkw.api.exceptions.MovieHallIdException;
import io.github.kkw.api.exceptions.ReservationNotFoundException;
import io.github.kkw.api.model.ClientId;
import io.github.kkw.api.model.ReservationId;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class HallReservationService {
    private final HallReservationRepository hallReservationRepository;
    private final MovieRepository movieRepository;

    public HallReservationService(HallReservationRepository hallReservationRepository, MovieRepository movieRepository) {
        this.hallReservationRepository = hallReservationRepository;
        this.movieRepository = movieRepository;
    }

    public ReservationId createHallReservation(ClientId clientId, int hallId, Date date) throws HallReservedException, MovieHallIdException {

        if (hallReservationRepository.isReserved(hallId, date)) {
            throw new HallReservedException("Hall with this ID is already reserved for chosen day");
        }
        if (!movieRepository.isHallIdValid(hallId)) {
            throw new MovieHallIdException("Wrong movie hall number");
        }
        hallReservationRepository.createHallReservation(clientId.getId(), hallId, date);
        return new ReservationId(hallReservationRepository.getHallReservationId(clientId.getId(), hallId, date));
    }

    public void deleteHallReservation(ClientId clientId, ReservationId reservationId) throws ReservationNotFoundException {
        if (hallReservationRepository.reservationDoesntExist(clientId.getId(), reservationId.getId())) {
            throw new ReservationNotFoundException("Reservation with this ID not found");
        }
        hallReservationRepository.deleteHallReservation(clientId.getId(), reservationId.getId());
    }
}
