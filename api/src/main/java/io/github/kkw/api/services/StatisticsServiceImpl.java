package io.github.kkw.api.services;

import io.github.kkw.api.db.HallReservationRepository;
import io.github.kkw.api.db.LoginRepository;
import io.github.kkw.api.db.MovieRepository;
import io.github.kkw.api.db.SeatReservationRepository;
import io.github.kkw.api.db.dto.ProfileEntity;
import io.github.kkw.api.exceptions.FromAfterToDateException;
import io.github.kkw.api.exceptions.FutureDatesException;
import io.github.kkw.api.exceptions.HallNoReservationsException;
import io.github.kkw.api.exceptions.HallNotFoundException;
import io.github.kkw.api.exceptions.MovieNotFoundException;
import io.github.kkw.api.exceptions.MovieShowsNotFoundException;
import io.github.kkw.api.model.HallStatistics;
import io.github.kkw.api.model.MovieStatistics;
import io.github.kkw.api.model.Statistics;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    private final SeatReservationRepository seatReservationRepository;
    private final HallReservationRepository hallReservationRepository;
    private final MovieRepository movieRepository;
    private final LoginRepository loginRepository;

    public StatisticsServiceImpl(SeatReservationRepository seatReservationRepository,
                                 HallReservationRepository hallReservationRepository,
                                 MovieRepository movieRepository,
                                 LoginRepository loginRepository){
        this.seatReservationRepository=seatReservationRepository;
        this.hallReservationRepository=hallReservationRepository;
        this.movieRepository=movieRepository;
        this.loginRepository=loginRepository;
    }

    private int clientsThatReservedHallOrSeat(){
        List<ProfileEntity> allClients = loginRepository.getAllProfiles();

        int clientsThatReserved = 0;
        for (final ProfileEntity profile : allClients) {

            if(seatReservationRepository.isClientReservedAnySeat(profile.getClientId())
                    || hallReservationRepository.isClientReservedAnyHall(profile.getClientId())){
                clientsThatReserved++;
            }
        }
        return clientsThatReserved;
    }

    public Statistics showStatistics(final Instant from, final Instant to) throws FutureDatesException, FromAfterToDateException {
        if(from.compareTo(to)>0){
            throw new FromAfterToDateException("From date cannot be after To date");
        }
        if(Instant.now().compareTo(from)<0 || Instant.now().compareTo(to)<0){
            throw new FutureDatesException("Dates from the future");
        }
        int seatReservations = seatReservationRepository.getSeatReservations(from,to);
        int hallReservations = hallReservationRepository.getHallReservations(from, to);
        int movies = movieRepository.getMovies(from, to);
        double moneyEarned = hallReservationRepository.getMoneyEarnedFromHalls(from,to)+seatReservationRepository.getMoneyEarned(from, to);
        int newClientsRegistered = loginRepository.newClientsRegistered(from, to);
        int totalClientsAtTheMoment = loginRepository.getTotalClientsAtTheMoment();
        int clientsThatReserved = clientsThatReservedHallOrSeat();
        return new Statistics(seatReservations, hallReservations,movies,moneyEarned,
                newClientsRegistered, totalClientsAtTheMoment,clientsThatReserved);
    }

    public MovieStatistics showStatisticsForMovie(String movieName) throws MovieNotFoundException, MovieShowsNotFoundException {
        int showCount = movieRepository.getMovieShowsCount(movieName);
        if(showCount==0){
            throw new MovieNotFoundException("There is not any movie named: "+movieName);
        }
        int totalReservations = seatReservationRepository.getReservationsCount(movieName);
        if(totalReservations==0){
            throw new MovieShowsNotFoundException("Cannot find any reservations for movie named: "+movieName);
        }
        Instant fromDate = movieRepository.getFirstShowingDateOfMovie(movieName);
        Instant toDate = movieRepository.getLastShowingDateOfMovie(movieName);
        double incomeGenerated = seatReservationRepository.getMovieIncomeGenerated(movieName).doubleValue();
        int deletedReservations = seatReservationRepository.getDeletedReservations(movieName);
        return new MovieStatistics(showCount,totalReservations,fromDate,toDate,incomeGenerated,deletedReservations);
    }

    public HallStatistics showStatisticsForHall(int hallId) throws HallNotFoundException, HallNoReservationsException {
        if(hallReservationRepository.hallDoesNotExist(hallId)){
            throw new HallNotFoundException("Hall "+ hallId +" doesn't exist");
        }
        int totalReservations = hallReservationRepository.getHallReservationsCounter(hallId).intValue();
        if(totalReservations==0){
            throw new HallNoReservationsException("Cannot find any reservations for hall of ID: "+hallId);
        }
        double totalIncome = hallReservationRepository.getIncomeGeneratedFromHall(hallId).doubleValue();
        int deletedReservations = hallReservationRepository.getDeletedReservations(hallId).intValue();
        return new HallStatistics(totalReservations, totalIncome, deletedReservations);
    }
}
