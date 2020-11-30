package io.github.kkw.api.controllers;

import io.github.kkw.api.exceptions.ClientNotLoggedInException;
import io.github.kkw.api.exceptions.FromAfterToDateException;
import io.github.kkw.api.exceptions.FutureDatesException;
import io.github.kkw.api.exceptions.HallNoReservationsException;
import io.github.kkw.api.exceptions.HallNotFoundException;
import io.github.kkw.api.exceptions.MovieConflictException;
import io.github.kkw.api.exceptions.MovieDateException;
import io.github.kkw.api.exceptions.MovieNotFoundException;
import io.github.kkw.api.exceptions.MovieShowsNotFoundException;
import io.github.kkw.api.exceptions.NotAdminException;
import io.github.kkw.api.exceptions.RestException;
import io.github.kkw.api.model.ClientId;
import io.github.kkw.api.model.HallStatistics;
import io.github.kkw.api.model.MovieAddRequest;
import io.github.kkw.api.model.MovieStatistics;
import io.github.kkw.api.model.Statistics;
import io.github.kkw.api.services.LoginService;
import io.github.kkw.api.services.MovieService;
import io.github.kkw.api.services.StatisticsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;

@RestController
public class AdminController implements CrossOriginMarker {
    private final LoginService loginService;
    private final MovieService movieService;
    private final StatisticsService statisticsService;

    public AdminController(LoginService loginService,
                           MovieService movieService,
                           StatisticsService statisticsService) {
        this.loginService = loginService;
        this.movieService = movieService;
        this.statisticsService=statisticsService;
    }

    @PostMapping("/addMovie")
    public void addMovie(@RequestParam("clientId") final ClientId clientId,
                         @Valid @RequestBody MovieAddRequest movieAddRequest) throws RestException {
        try{
            loginService.verifyAdmin(clientId);
            loginService.verifyClientLoggedIn(clientId);
            movieService.addMovie(movieAddRequest.getName(), movieAddRequest.getStartDate(), movieAddRequest.getEndDate(),
                    movieAddRequest.getBasePrice(), movieAddRequest.getHallId(), movieAddRequest.getDescription(), movieAddRequest.getLink());
        } catch (MovieDateException | HallNotFoundException e){
            throw new RestException(e.getMessage(), HttpStatus.BAD_REQUEST, e);
        } catch (MovieConflictException e){
            throw new RestException(e.getMessage(), HttpStatus.CONFLICT, e);
        } catch (ClientNotLoggedInException | NotAdminException e) {
            throw new RestException(e.getMessage(), HttpStatus.FORBIDDEN, e);
        }
    }

    @GetMapping("/showStatistics")
    public Statistics showStatistics(@RequestParam("clientId") final ClientId clientId,
                                     @RequestParam("from") final Instant from,
                                     @RequestParam("to") final Instant to) throws RestException {
        try{
            loginService.verifyAdmin(clientId);
            loginService.verifyClientLoggedIn(clientId);
            return statisticsService.showStatistics(from,to);
        } catch (FutureDatesException | FromAfterToDateException e){
            throw new RestException(e.getMessage(), HttpStatus.BAD_REQUEST, e);
        } catch (ClientNotLoggedInException | NotAdminException e) {
            throw new RestException(e.getMessage(), HttpStatus.FORBIDDEN, e);
        }
    }

    @GetMapping("/showStatistics/movie/{movieName}")
    public MovieStatistics showStatisticsForMovie(@RequestParam("clientId") final ClientId clientId,
                                                  @PathVariable("movieName") @NotEmpty final String movieName) throws RestException {
        try {
            loginService.verifyAdmin(clientId);
            loginService.verifyClientLoggedIn(clientId);
            return statisticsService.showStatisticsForMovie(movieName);
        } catch (MovieNotFoundException e) {
            throw new RestException(e.getMessage(), HttpStatus.BAD_REQUEST, e);
        } catch (MovieShowsNotFoundException e) {
            throw new RestException(e.getMessage(), HttpStatus.NOT_FOUND, e);
        } catch (ClientNotLoggedInException | NotAdminException e) {
            throw new RestException(e.getMessage(), HttpStatus.FORBIDDEN, e);
        }
    }

    @GetMapping("/showStatistics/hall/{hallId}")
    public HallStatistics showStatisticsForHall(@RequestParam("clientId") final ClientId clientId,
                                                @PathVariable("hallId") final int hallId) throws RestException {
        try {
            loginService.verifyAdmin(clientId);
            loginService.verifyClientLoggedIn(clientId);
            return statisticsService.showStatisticsForHall(hallId);
        } catch (HallNotFoundException e) {
            throw new RestException(e.getMessage(), HttpStatus.BAD_REQUEST, e);
        } catch (HallNoReservationsException e) {
            throw new RestException(e.getMessage(), HttpStatus.NOT_FOUND, e);
        } catch (ClientNotLoggedInException | NotAdminException e) {
            throw new RestException(e.getMessage(), HttpStatus.FORBIDDEN, e);
        }
    }

    @GetMapping("/verifyAdmin")
    public void verifyAdmin(@RequestParam("clientId") final ClientId clientId) throws RestException {
        try{
            loginService.verifyAdmin(clientId);
        } catch (NotAdminException e) {
            throw new RestException(e.getMessage(), HttpStatus.FORBIDDEN, e);
        }
    }
}
