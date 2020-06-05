package io.github.kkw.api.controllers;

import io.github.kkw.api.exceptions.ClientExistsException;
import io.github.kkw.api.exceptions.ClientNotFoundException;
import io.github.kkw.api.exceptions.ClientNotLoggedInException;
import io.github.kkw.api.exceptions.RestException;
import io.github.kkw.api.exceptions.WrongPasswordException;
import io.github.kkw.api.model.ClientId;
import io.github.kkw.api.model.Profile;
import io.github.kkw.api.services.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Optional;

@RestController
@Validated
public class LoginController implements CrossOriginMarker {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/register")
    public ClientId registerClient(@Email @RequestParam("email") @NotEmpty String email,
                                   @RequestParam("password") @NotEmpty String password,
                                   @RequestParam(value = "name", required = false) Optional<String> name,
                                   @RequestParam(value = "surname", required = false) Optional<String> surname) throws RestException {
        try {
            return loginService.registerClient(email, password, name, surname);
        } catch (ClientExistsException e) {
            throw new RestException(e.getMessage(), HttpStatus.CONFLICT, e);
        }
    }

    @PostMapping("/login")
    public ClientId loginClient(@Email @RequestParam("email") @NotEmpty String email,
                                @RequestParam("password") @NotEmpty String password) throws RestException {
        try {
            loginService.verifyEmailExists(email);
            return loginService.loginClient(email, password);
        } catch (ClientNotFoundException e) {
            throw new RestException(e.getMessage(), HttpStatus.NOT_FOUND, e);
        } catch (WrongPasswordException e) {
            throw new RestException(e.getMessage(), HttpStatus.FORBIDDEN, e);
        }
    }

    @PostMapping("/logout")
    public void logoutClient(@RequestParam("clientId") ClientId clientId) throws RestException {
        try {
            loginService.verifyClientLoggedIn(clientId);
            loginService.logoutClient(clientId);
        } catch (ClientNotLoggedInException e) {
            throw new RestException(e.getMessage(), HttpStatus.BAD_REQUEST, e);
        }
    }

    @GetMapping("/showProfile")
    public Profile showProfile(@RequestParam("clientId") ClientId clientId) throws RestException {
        try {
            loginService.verifyClientLoggedIn(clientId);
            return loginService.showProfile(clientId);
        } catch (ClientNotLoggedInException e) {
            throw new RestException(e.getMessage(), HttpStatus.FORBIDDEN, e);
        }
    }
}
