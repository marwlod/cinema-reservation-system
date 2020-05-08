package io.github.kkw.api.controllers;

import io.github.kkw.api.exceptions.*;
import io.github.kkw.api.model.*;
import io.github.kkw.api.services.HallService;
import io.github.kkw.api.services.LoginService;
import io.github.kkw.api.services.MovieService;
import io.github.kkw.api.services.SeatReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;

@RestController
public class SeatReservationController {
    private final LoginService loginService;
    private final SeatReservationService seatReservationService;
    private final MovieService movieService;
    private final HallService hallService;

    public SeatReservationController(final LoginService loginService,
                                     final SeatReservationService seatReservationService,
                                     final MovieService movieService,
                                     final HallService hallService) {
        this.loginService = loginService;
        this.seatReservationService = seatReservationService;
        this.movieService = movieService;
        this.hallService=hallService;
    }

    @PostMapping("/reserveSeat/{movieId}/{seatId}")
    public ReservationId createSeatReservation(@RequestParam("clientId") final ClientId clientId,
                                               @PathVariable("movieId") int movieId,
                                               @PathVariable("seatId") int seatId) throws RestException {
        try {
            loginService.verifyClientLoggedIn(clientId);
            return seatReservationService.createSeatReservation(clientId, movieId, seatId);
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

    @PostMapping("/addMovie")
    public void addMovie(@RequestParam("clientId") final ClientId clientId,
                         @Valid @RequestBody MovieAddRequest movieAddRequest) throws RestException {
        try{
            loginService.verifyClientLoggedIn(clientId);
            movieService.addMovie(movieAddRequest.getName(), movieAddRequest.getStartDate(), movieAddRequest.getEndDate(),
                    movieAddRequest.getBasePrice(), movieAddRequest.getHallId());
        } catch (MovieDateException | MovieHallIdException e){
            throw new RestException(e.getMessage(), HttpStatus.BAD_REQUEST, e);
        } catch (MovieConflictException e){
            throw new RestException(e.getMessage(), HttpStatus.CONFLICT, e);
        } catch (ClientNotLoggedInException e) {
            throw new RestException(e.getMessage(), HttpStatus.FORBIDDEN, e);
        }
    }

    @GetMapping("/showCinemaHalls")
    List<Hall> showCinemaHalls(@RequestParam("clientId") final ClientId clientId) throws RestException {
        try{
            loginService.verifyClientLoggedIn(clientId);
            return hallService.showCinemaHalls();
        } catch (CinemaHallNotFoundException e) {
            throw new RestException(e.getMessage(),HttpStatus.BAD_REQUEST,e);
        } catch (ClientNotLoggedInException e){
            throw new RestException(e.getMessage(),HttpStatus.FORBIDDEN,e);
        }
    }

    @GetMapping("/showAvailableCinemaHalls")
    List<Hall> showAvailableCinemaHalls(@RequestParam("clientId") final ClientId clientId,
                                        @RequestParam("date") Instant date) throws ClientNotLoggedInException, RestException {
        try {
            loginService.verifyClientLoggedIn(clientId);
            return hallService.showAvailableCinemaHalls(date);
        } catch (AvailableCinemaHallsNotFoundException e){
            throw new RestException(e.getMessage(),HttpStatus.NOT_FOUND,e);
        } catch (ClientNotLoggedInException e) {
            throw new RestException(e.getMessage(), HttpStatus.FORBIDDEN, e);
        }
    }

}
