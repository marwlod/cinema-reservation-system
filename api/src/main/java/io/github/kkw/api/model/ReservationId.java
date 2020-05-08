package io.github.kkw.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReservationId {
    private final int reservationId;

    public ReservationId(@JsonProperty("reservationId") int reservationId) {
        this.reservationId = reservationId;
    }

    public ReservationId(String reservationId) {
        this.reservationId = Integer.parseInt(reservationId);
    }

    public int getReservationId() {
        return reservationId;
    }
}
