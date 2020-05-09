package io.github.kkw.api.controllers;

import io.github.kkw.api.exceptions.ClientNotLoggedInException;
import io.github.kkw.api.exceptions.HallNotFoundException;
import io.github.kkw.api.exceptions.MovieConflictException;
import io.github.kkw.api.exceptions.MovieDateException;
import io.github.kkw.api.exceptions.NotAdminException;
import io.github.kkw.api.exceptions.RestException;
import io.github.kkw.api.model.ClientId;
import io.github.kkw.api.model.MovieAddRequest;
import io.github.kkw.api.services.LoginService;
import io.github.kkw.api.services.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AdminController {
    private final LoginService loginService;
    private final MovieService movieService;

    public AdminController(LoginService loginService, MovieService movieService) {
        this.loginService = loginService;
        this.movieService = movieService;
    }

    @PostMapping("/addMovie")
    public void addMovie(@RequestParam("clientId") final ClientId clientId,
                         @Valid @RequestBody MovieAddRequest movieAddRequest) throws RestException {
        try{
            loginService.verifyAdmin(clientId);
            loginService.verifyClientLoggedIn(clientId);
            movieService.addMovie(movieAddRequest.getName(), movieAddRequest.getStartDate(), movieAddRequest.getEndDate(),
                    movieAddRequest.getBasePrice(), movieAddRequest.getHallId());
        } catch (MovieDateException | HallNotFoundException e){
            throw new RestException(e.getMessage(), HttpStatus.BAD_REQUEST, e);
        } catch (MovieConflictException e){
            throw new RestException(e.getMessage(), HttpStatus.CONFLICT, e);
        } catch (ClientNotLoggedInException | NotAdminException e) {
            throw new RestException(e.getMessage(), HttpStatus.FORBIDDEN, e);
        }
    }
}
