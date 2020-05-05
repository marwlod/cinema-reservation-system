package io.github.kkw.api.controllers;

import io.github.kkw.api.exceptions.ClientNotLoggedInException;
import io.github.kkw.api.exceptions.MovieNotFoundException;
import io.github.kkw.api.exceptions.ReservationNotFoundException;
import io.github.kkw.api.exceptions.RestException;
import io.github.kkw.api.exceptions.SeatNotFoundException;
import io.github.kkw.api.exceptions.SeatReservedException;
import io.github.kkw.api.model.ClientId;
import io.github.kkw.api.model.ReservationId;
import io.github.kkw.api.services.LoginService;
import io.github.kkw.api.services.SeatReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SeatReservationController {
    private final LoginService loginService;
    private final SeatReservationService seatReservationService;

    public SeatReservationController(final LoginService loginService,
                                     final SeatReservationService seatReservationService) {
        this.loginService = loginService;
        this.seatReservationService = seatReservationService;
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
}
