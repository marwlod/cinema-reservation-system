package io.github.kkw.api.services;

import io.github.kkw.api.db.HallReservationRepository;
import io.github.kkw.api.db.MovieRepository;
import io.github.kkw.api.db.dto.HallEntity;
import io.github.kkw.api.exceptions.AvailableCinemaHallsNotFoundException;
import io.github.kkw.api.exceptions.CinemaHallsNotFoundException;
import io.github.kkw.api.exceptions.DateTooSoonException;
import io.github.kkw.api.exceptions.HallReservedException;
import io.github.kkw.api.exceptions.HallNotFoundException;
import io.github.kkw.api.exceptions.ReservationNotFoundException;
import io.github.kkw.api.model.ClientId;
import io.github.kkw.api.model.Hall;
import io.github.kkw.api.model.ReservationId;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HallReservationService {
    private final HallReservationRepository hallReservationRepository;
    private final MovieRepository movieRepository;

    public HallReservationService(HallReservationRepository hallReservationRepository, MovieRepository movieRepository) {
        this.hallReservationRepository = hallReservationRepository;
        this.movieRepository = movieRepository;
    }

    public ReservationId createHallReservation(ClientId clientId, int hallId, Instant date) throws HallReservedException, HallNotFoundException {
        if (hallReservationRepository.isReserved(hallId, date)) {
            throw new HallReservedException("Hall with this ID is already reserved for chosen day");
        }
        if (!movieRepository.isHallIdValid(hallId)) {
            throw new HallNotFoundException("Hall with this ID not found");
        }
        hallReservationRepository.createHallReservation(clientId.getClientId(), hallId, date);
        return new ReservationId(hallReservationRepository.getHallReservationId(clientId.getClientId(), hallId, date));
    }

    public void deleteHallReservation(ClientId clientId, ReservationId reservationId) throws ReservationNotFoundException {
        if (hallReservationRepository.reservationDoesntExist(clientId.getClientId(), reservationId.getReservationId())) {
            throw new ReservationNotFoundException("Reservation with this ID not found");
        }
        hallReservationRepository.deleteHallReservation(clientId.getClientId(), reservationId.getReservationId());
    }

    public List<Hall> showCinemaHalls() throws CinemaHallsNotFoundException {
        List<HallEntity> hallEntities = hallReservationRepository.showCinemaHalls();
        if(hallEntities.isEmpty()){
            throw new CinemaHallsNotFoundException("Cannot find any cinema halls");
        }
        return hallEntities.stream().map(Hall::new).collect(Collectors.toList());
    }

    public List<Hall> showAvailableCinemaHalls(final Instant date) throws AvailableCinemaHallsNotFoundException, DateTooSoonException {
        long daysFromNowToDate = Instant.now().until(date, ChronoUnit.DAYS);
        if (daysFromNowToDate < 14) {
            throw new DateTooSoonException("Date must be at least 14 days in the future");
        }
        List<HallEntity> hallEntities = hallReservationRepository.showAvailableCinemaHalls(date);
        if(hallEntities.isEmpty()){
            throw new AvailableCinemaHallsNotFoundException("Cannot find any cinema halls in this term");
        }
        return hallEntities.stream().map(Hall::new).collect(Collectors.toList());
    }
}
