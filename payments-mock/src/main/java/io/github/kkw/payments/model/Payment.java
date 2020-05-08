package io.github.kkw.payments.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class Payment {
    private int clientId;
    private double amount;
    private String method;
    private int reservationId;

    @NotEmpty
    @Email
    private String email;

    @Pattern(regexp = "^[0-9]{16}$", message = "Hmm that seems fake, real cards have 16 digits")
    private String creditCardNumber;

    @Pattern(regexp = "^[01][0-9]/[0-9]{4}$", message = "Looking for a date in MM/yyyy format")
    private String expires;

    @Pattern(regexp = "^[0-9]{3}$", message = "3 digits needed")
    private String CVV;

    public Payment() {
    }

    public Payment(int clientId, double amount, String method, int reservationId) {
        this.clientId = clientId;
        this.amount = amount;
        this.method = method;
        this.reservationId = reservationId;
    }

    public int getClientId() {
        return clientId;
    }

    public double getAmount() {
        return amount;
    }

    public String getMethod() {
        return method;
    }

    public int getReservationId() {
        return reservationId;
    }

    public String getEmail() {
        return email;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public String getExpires() {
        return expires;
    }

    public String getCVV() {
        return CVV;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public void setCVV(String CVV) {
        this.CVV = CVV;
    }
}
