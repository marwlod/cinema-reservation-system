package io.github.kkw.api.controllers;

import io.github.kkw.api.exceptions.ClientExistsException;
import io.github.kkw.api.exceptions.ClientNotFoundException;
import io.github.kkw.api.exceptions.RestException;
import io.github.kkw.api.exceptions.WrongPasswordException;
import io.github.kkw.api.model.ClientId;
import io.github.kkw.api.services.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Email;
import java.util.Optional;

@RestController
@Validated
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/register")
    public ClientId registerClient(@Email @RequestParam("email") String email,
                                   @RequestParam("password") String password,
                                   @RequestParam(value = "name", required = false) Optional<String> name,
                                   @RequestParam(value = "surname", required = false) Optional<String> surname) throws RestException {
        try {
            return loginService.registerClient(email, password, name, surname);
        } catch (ClientExistsException e) {
            throw new RestException(e.getMessage(), HttpStatus.CONFLICT, e.getCause());
        }
    }

    @PostMapping("/login")
    public ClientId loginClient(@Email @RequestParam("email") String email,
                              @RequestParam("password") String password) throws RestException {
        try {
            loginService.verifyEmailExists(email);
            return loginService.loginClient(email, password);
        } catch (ClientNotFoundException e) {
            throw new RestException(e.getMessage(), HttpStatus.NOT_FOUND, e.getCause());
        } catch (WrongPasswordException e) {
            throw new RestException(e.getMessage(), HttpStatus.FORBIDDEN, e.getCause());
        }
    }

    @PostMapping("/logout")
    public void logoutClient(@Email @RequestParam("email") String email) throws RestException {
        try {
            loginService.verifyEmailExists(email);
            loginService.logoutClient(email);
        } catch (ClientNotFoundException e) {
            throw new RestException(e.getMessage(), HttpStatus.NOT_FOUND, e.getCause());
        }
    }
}
