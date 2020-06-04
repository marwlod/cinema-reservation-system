package io.github.kkw.api.controllers;

import io.github.kkw.api.exceptions.ClientNotLoggedInException;
import io.github.kkw.api.exceptions.MovieNotFoundException;
import io.github.kkw.api.exceptions.MoviesNotFoundException;
import io.github.kkw.api.exceptions.NoFreeSeatsException;
import io.github.kkw.api.exceptions.ReservationNotFoundException;
import io.github.kkw.api.exceptions.RestException;
import io.github.kkw.api.exceptions.SeatNotFoundException;
import io.github.kkw.api.exceptions.SeatReservedException;
import io.github.kkw.api.model.ClientId;
import io.github.kkw.api.model.Movie;
import io.github.kkw.api.model.ReservationId;
import io.github.kkw.api.model.Seat;
import io.github.kkw.api.model.SeatReservation;
import io.github.kkw.api.services.LoginService;
import io.github.kkw.api.services.MovieService;
import io.github.kkw.api.services.SeatReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
public class SeatReservationController implements CrossOriginMarker {
    private final LoginService loginService;
    private final SeatReservationService seatReservationService;
    private final MovieService movieService;

    public SeatReservationController(final LoginService loginService,
                                     final SeatReservationService seatReservationService,
                                     final MovieService movieService) {
        this.loginService = loginService;
        this.seatReservationService = seatReservationService;
        this.movieService = movieService;
    }

    @PostMapping("/reserveSeat/{movieId}/{seatId}")
    public ReservationId createSeatReservation(@RequestParam("clientId") final ClientId clientId,
                                               @RequestParam(value = "code", required = false) Optional<String> code,
                                               @PathVariable("movieId") int movieId,
                                               @PathVariable("seatId") int seatId) throws RestException {
        try {
            loginService.verifyClientLoggedIn(clientId);
            return seatReservationService.createSeatReservation(clientId, movieId, seatId, code);
        } catch (MovieNotFoundException | SeatNotFoundException e) {
            throw new RestException(e.getMessage(), HttpStatus.NOT_FOUND, e);
        } catch (ClientNotLoggedInException e) {
            throw new RestException(e.getMessage(), HttpStatus.FORBIDDEN, e);
        } catch (SeatReservedException e) {
            throw new RestException(e.getMessage(), HttpStatus.CONFLICT, e);
        }
    }

    @DeleteMapping("/reserveSeat/{reservationId}")
    public void deleteSeatReservation(@RequestParam("clientId") final ClientId clientId,
                                      @PathVariable("reservationId") final ReservationId reservationId) throws RestException {
        try {
            loginService.verifyClientLoggedIn(clientId);
            seatReservationService.deleteSeatReservation(clientId, reservationId);
        } catch (ReservationNotFoundException e) {
            throw new RestException(e.getMessage(), HttpStatus.NOT_FOUND, e);
        } catch (ClientNotLoggedInException e) {
            throw new RestException(e.getMessage(), HttpStatus.FORBIDDEN, e);
        }
    }

    @GetMapping("/showMovies")
    public @ResponseBody
    List<Movie> showProgramme(@RequestParam("from") Instant from,
                              @RequestParam("to") Instant to) throws RestException {
        try{
            return movieService.showProgramme(from, to);
        } catch (MoviesNotFoundException e) {
            throw new RestException(e.getMessage(), HttpStatus.NOT_FOUND,e);
        }
    }

    @GetMapping("/showFreeSeats/{movieId}")
    public List<Seat> showFreeSeats(@RequestParam("clientId") final ClientId clientId,
                                    @PathVariable("movieId") final int movieId) throws RestException {
        try {
            loginService.verifyClientLoggedIn(clientId);
            return seatReservationService.showFreeSeats(movieId);
        } catch (NoFreeSeatsException | MovieNotFoundException e) {
            throw new RestException(e.getMessage(), HttpStatus.NOT_FOUND, e);
        } catch (ClientNotLoggedInException e) {
            throw new RestException(e.getMessage(), HttpStatus.FORBIDDEN, e);
        }
    }

    @GetMapping("/showAllSeats/{movieId}")
    public List<Seat> showAllSeats(@RequestParam("clientId") final ClientId clientId,
                                   @PathVariable("movieId") final int movieId) throws RestException {
        try {
            loginService.verifyClientLoggedIn(clientId);
            return seatReservationService.showAllSeats(movieId);
        } catch (MovieNotFoundException e) {
            throw new RestException(e.getMessage(), HttpStatus.NOT_FOUND, e);
        } catch (ClientNotLoggedInException e) {
            throw new RestException(e.getMessage(), HttpStatus.FORBIDDEN, e);
        }
    }

    @GetMapping("/showReservations/seat")
    public List<SeatReservation> showReservations(@RequestParam("clientId") final ClientId clientId,
                                                  @RequestParam("from") final Instant from,
                                                  @RequestParam("to") final Instant to) throws RestException {
        try {
            loginService.verifyClientLoggedIn(clientId);
            return seatReservationService.showReservations(clientId, from, to);
        } catch (ClientNotLoggedInException e) {
            throw new RestException(e.getMessage(), HttpStatus.FORBIDDEN, e);
        }
    }
}
