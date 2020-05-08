package io.github.kkw.api.controllers;

import io.github.kkw.api.exceptions.ClientNotLoggedInException;
import io.github.kkw.api.exceptions.ReservationAlreadyPaid;
import io.github.kkw.api.exceptions.ReservationNotFoundException;
import io.github.kkw.api.exceptions.RestException;
import io.github.kkw.api.model.ClientId;
import io.github.kkw.api.model.ReservationId;
import io.github.kkw.api.services.LoginService;
import io.github.kkw.api.services.PaymentsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.net.URL;

@RestController
public class PaymentsController {
    private final LoginService loginService;
    private final PaymentsService paymentsService;

    public PaymentsController(LoginService loginService, PaymentsService paymentsService) {
        this.loginService = loginService;
        this.paymentsService = paymentsService;
    }

    @PostMapping("/payForSeat/{reservationId}")
    public URL payForSeat(@RequestParam("clientId") ClientId clientId,
                          @PathVariable("reservationId") ReservationId reservationId) throws RestException, MalformedURLException {
        try {
            loginService.verifyClientLoggedIn(clientId);
            double paymentAmount = paymentsService.getPaymentAmountForSeat(clientId, reservationId);
            return buildURL(clientId, reservationId, paymentAmount, "seat");
        } catch (ClientNotLoggedInException e) {
            throw new RestException(e.getMessage(), HttpStatus.FORBIDDEN, e);
        } catch (ReservationNotFoundException e) {
            throw new RestException(e.getMessage(), HttpStatus.NOT_FOUND, e);
        } catch (ReservationAlreadyPaid e) {
            throw new RestException(e.getMessage(), HttpStatus.BAD_REQUEST, e);
        }
    }

    @PostMapping("/payAdvanceForHall/{reservationId}")
    public URL payAdvanceForHall(@RequestParam("clientId") ClientId clientId,
                                @PathVariable("reservationId") ReservationId reservationId) throws RestException, MalformedURLException {
        try {
            loginService.verifyClientLoggedIn(clientId);
            double paymentAmount = paymentsService.getAdvancePaymentAmountForHall(clientId, reservationId);
            return buildURL(clientId, reservationId, paymentAmount, "advanceHall");
        } catch (ClientNotLoggedInException e) {
            throw new RestException(e.getMessage(), HttpStatus.FORBIDDEN, e);
        } catch (ReservationNotFoundException e) {
            throw new RestException(e.getMessage(), HttpStatus.NOT_FOUND, e);
        } catch (ReservationAlreadyPaid e) {
            throw new RestException(e.getMessage(), HttpStatus.BAD_REQUEST, e);
        }
    }

    @PostMapping("/payForHall/{reservationId}")
    public URL payForHall(@RequestParam("clientId") ClientId clientId,
                          @PathVariable("reservationId") ReservationId reservationId) throws RestException, MalformedURLException {
        try {
            loginService.verifyClientLoggedIn(clientId);
            double paymentAmount = paymentsService.getPaymentAmountForHall(clientId, reservationId);
            return buildURL(clientId, reservationId, paymentAmount, "hall");
        } catch (ClientNotLoggedInException e) {
            throw new RestException(e.getMessage(), HttpStatus.FORBIDDEN, e);
        } catch (ReservationNotFoundException e) {
            throw new RestException(e.getMessage(), HttpStatus.NOT_FOUND, e);
        } catch (ReservationAlreadyPaid e) {
            throw new RestException(e.getMessage(), HttpStatus.BAD_REQUEST, e);
        }
    }

    @PostMapping("/finishPaymentSeat/{reservationId}")
    public void finishPaymentSeat(@RequestParam("password") String password,
                                  @RequestParam("clientId") ClientId clientId,
                                  @PathVariable("reservationId") ReservationId reservationId) throws RestException {
        verifyPassword(password);
        paymentsService.finishPaymentSeat(clientId, reservationId);
    }

    @PostMapping("/finishAdvancePaymentHall/{reservationId}")
    public void finishAdvancePaymentHall(@RequestParam("password") String password,
                                         @RequestParam("clientId") ClientId clientId,
                                         @PathVariable("reservationId") ReservationId reservationId) throws RestException {
        verifyPassword(password);
        paymentsService.finishAdvancePaymentHall(clientId, reservationId);
    }

    @PostMapping("/finishPaymentHall/{reservationId}")
    public void finishPaymentHall(@RequestParam("password") String password,
                                  @RequestParam("clientId") ClientId clientId,
                                  @PathVariable("reservationId") ReservationId reservationId) throws RestException {
        verifyPassword(password);
        paymentsService.finishPaymentHall(clientId, reservationId);
    }

    private void verifyPassword(String password) throws RestException {
        if (!password.equals("supersecretTO2")) {
            throw new RestException("Wrong password, bye bye.", HttpStatus.FORBIDDEN, null);
        }
    }

    private URL buildURL(ClientId clientId, ReservationId reservationId, double paymentAmount, String type) throws MalformedURLException {
        return new URL("http://localhost:9090/pay/" + reservationId.getId() + "?clientId=" + clientId.getId() + "&amount=" + paymentAmount + "&type=" + type);
    }
}
