package io.github.kkw.api.controllers;

import io.github.kkw.api.exceptions.AvailableCinemaHallsNotFoundException;
import io.github.kkw.api.exceptions.CinemaHallsNotFoundException;
import io.github.kkw.api.exceptions.ClientNotLoggedInException;
import io.github.kkw.api.exceptions.DateTooSoonException;
import io.github.kkw.api.exceptions.HallReservedException;
import io.github.kkw.api.exceptions.MovieHallIdException;
import io.github.kkw.api.exceptions.ReservationNotFoundException;
import io.github.kkw.api.exceptions.RestException;
import io.github.kkw.api.model.ClientId;
import io.github.kkw.api.model.Hall;
import io.github.kkw.api.model.ReservationId;
import io.github.kkw.api.services.HallReservationService;
import io.github.kkw.api.services.LoginService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
public class HallReservationController {
    private final LoginService loginService;
    private final HallReservationService hallReservationService;

    public HallReservationController(final LoginService loginService,
                                     final HallReservationService hallReservationService) {
        this.loginService = loginService;
        this.hallReservationService = hallReservationService;
    }

    @PostMapping("/reserveHall/{hallId}/{date}")
    public ReservationId createHallReservation(@RequestParam("clientId") final ClientId clientId,
                                               @PathVariable("hallId") int hallId,
                                               @PathVariable("date") Instant date) throws RestException {
        try {
            System.out.println(date);
            loginService.verifyClientLoggedIn(clientId);
            return hallReservationService.createHallReservation(clientId, hallId, date);
        } catch (MovieHallIdException e) {
            throw new RestException(e.getMessage(), HttpStatus.NOT_FOUND, e);
        } catch (ClientNotLoggedInException e) {
            throw new RestException(e.getMessage(), HttpStatus.FORBIDDEN, e);
        } catch (HallReservedException e) {
            throw new RestException(e.getMessage(), HttpStatus.CONFLICT, e);
        }
    }

    @DeleteMapping("/reserveHall/{reservationId}")
    public void deleteHallReservation(@RequestParam("clientId") final ClientId clientId,
                                      @PathVariable("reservationId") final ReservationId reservationId) throws RestException {
        try {
            loginService.verifyClientLoggedIn(clientId);
            hallReservationService.deleteHallReservation(clientId, reservationId);
        } catch (ReservationNotFoundException e) {
            throw new RestException(e.getMessage(), HttpStatus.NOT_FOUND, e);
        } catch (ClientNotLoggedInException e) {
            throw new RestException(e.getMessage(), HttpStatus.FORBIDDEN, e);
        }
    }

    @GetMapping("/showCinemaHalls")
    List<Hall> showCinemaHalls(@RequestParam("clientId") final ClientId clientId) throws RestException {
        try{
            loginService.verifyClientLoggedIn(clientId);
            return hallReservationService.showCinemaHalls();
        } catch (CinemaHallsNotFoundException e) {
            throw new RestException(e.getMessage(),HttpStatus.NOT_FOUND,e);
        } catch (ClientNotLoggedInException e){
            throw new RestException(e.getMessage(),HttpStatus.FORBIDDEN,e);
        }
    }

    @GetMapping("/showAvailableCinemaHalls")
    List<Hall> showAvailableCinemaHalls(@RequestParam("clientId") final ClientId clientId,
                                        @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Instant date) throws RestException {
        try {
            loginService.verifyClientLoggedIn(clientId);
            return hallReservationService.showAvailableCinemaHalls(date);
        } catch (AvailableCinemaHallsNotFoundException e){
            throw new RestException(e.getMessage(), HttpStatus.NOT_FOUND, e);
        } catch (ClientNotLoggedInException e) {
            throw new RestException(e.getMessage(), HttpStatus.FORBIDDEN, e);
        } catch (DateTooSoonException e) {
            throw new RestException(e.getMessage(), HttpStatus.BAD_REQUEST, e);
        }
    }
}
