package io.github.kkw.api.controllers;

import io.github.kkw.api.exceptions.*;
import io.github.kkw.api.model.*;
import io.github.kkw.api.services.LoginService;
import io.github.kkw.api.services.MovieService;
import io.github.kkw.api.services.SpecialOffersService;
import io.github.kkw.api.services.StatisticsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;
import java.util.List;

@RestController
public class AdminController implements CrossOriginMarker {
    private final LoginService loginService;
    private final MovieService movieService;
    private final StatisticsService statisticsService;
    private final SpecialOffersService specialOffersService;

    public AdminController(LoginService loginService,
                           MovieService movieService,
                           StatisticsService statisticsService,
                           SpecialOffersService specialOffersService) {
        this.loginService = loginService;
        this.movieService = movieService;
        this.statisticsService=statisticsService;
        this.specialOffersService=specialOffersService;
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
            return statisticsService.showStatistics(clientId,from,to);
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

    @PostMapping("/addSpecialOffer")
    public void addSpecialOffer(@RequestParam("clientId") final ClientId clientId,
                                @Valid @RequestBody SpecialOfferAddRequest specialOffer) throws RestException {
        try {
            loginService.verifyAdmin(clientId);
            loginService.verifyClientLoggedIn(clientId);
            specialOffersService.addSpecialOffer(specialOffer);
        } catch (SpecialCodeAlreadyExistsException e){
            throw new RestException(e.getMessage(),HttpStatus.CONFLICT, e);
        } catch (ClientNotLoggedInException | NotAdminException e) {
            throw new RestException(e.getMessage(), HttpStatus.FORBIDDEN, e);
        }
    }

    @GetMapping("/showSpecialOffers")
    public @ResponseBody
    List<SpecialOffer> showSpecialOffers(@RequestParam("clientId") final ClientId clientId) throws RestException {
        try{
            loginService.verifyAdmin(clientId);
            loginService.verifyClientLoggedIn(clientId);
            return specialOffersService.showSpecialOffers();

        }  catch (SpecialOffersNotFoundException e){
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
