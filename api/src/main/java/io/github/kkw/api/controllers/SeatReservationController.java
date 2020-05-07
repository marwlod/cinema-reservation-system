package io.github.kkw.api.controllers;

import io.github.kkw.api.exceptions.*;
import io.github.kkw.api.model.ClientId;
import io.github.kkw.api.model.Movie;
import io.github.kkw.api.model.ReservationId;
import io.github.kkw.api.services.LoginService;
import io.github.kkw.api.services.MovieService;
import io.github.kkw.api.services.SeatReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
public class SeatReservationController {
    private final LoginService loginService;
    private final SeatReservationService seatReservationService;
    private final MovieService movieService;

    public SeatReservationController(final LoginService loginService,
                                     final SeatReservationService seatReservationService, MovieService movieService) {
        this.loginService = loginService;
        this.seatReservationService = seatReservationService;
        this.movieService = movieService;
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
            throw new RestException(e.getMessage(), HttpStatus.BAD_REQUEST, e);
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
            throw new RestException(e.getMessage(), HttpStatus.BAD_REQUEST, e);
        }
    }

    @GetMapping("/showMovies")
    public @ResponseBody
    List<Movie> showCurrentProgramme() throws RestException {
        try{
            return movieService.showProgramme();
        } catch (MoviesNotFoundException e) {
            throw new RestException(e.getMessage(), HttpStatus.NOT_FOUND,e);
        }
    }

    @PostMapping("/addMovie/{name}/{start_date}/{end_date}/{base_price}/{hall_id}")
    public void addMovie(@PathVariable("name") String name,
                         @PathVariable("start_date") Instant start_date,
                         @PathVariable("end_date") Instant end_date,
                         @PathVariable("base_price") double base_price,
                         @PathVariable("hall_id") int hall_id) throws RestException {
        try{
            movieService.addMovie(name,start_date,end_date,base_price,hall_id);
        } catch (MovieDateException | MovieMissingValuesException | MovieHallIdException e){
            throw new RestException(e.getMessage(), HttpStatus.BAD_REQUEST,e);
        } catch (MovieConflictException e){
            throw new RestException(e.getMessage(),HttpStatus.CONFLICT, e);
        }
    }
}
