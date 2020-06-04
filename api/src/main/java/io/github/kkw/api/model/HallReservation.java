package io.github.kkw.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.kkw.api.db.dto.HallReservationEntity;

import java.time.Instant;

public class HallReservation {
    private final int hallReservationId;
    private final Instant validUntil;
    private final boolean isPaidAdvance;
    private final boolean isPaidTotal;
    private final double advancePrice;
    private final double totalPrice;
    private final Instant reservationDate;
    private final int screenSize;

    public HallReservation(@JsonProperty("hallReservationId") int hallReservationId,
                           @JsonProperty("validUntil") Instant validUntil,
                           @JsonProperty("isPaidAdvance") boolean isPaidAdvance,
                           @JsonProperty("isPaidTotal") boolean isPaidTotal,
                           @JsonProperty("advancePrice") double advancePrice,
                           @JsonProperty("totalPrice") double totalPrice,
                           @JsonProperty("reservationDate") Instant reservationDate,
                           @JsonProperty("screenSize") int screenSize) {
        this.hallReservationId = hallReservationId;
        this.validUntil = validUntil;
        this.isPaidAdvance = isPaidAdvance;
        this.isPaidTotal = isPaidTotal;
        this.advancePrice = advancePrice;
        this.totalPrice = totalPrice;
        this.reservationDate = reservationDate;
        this.screenSize = screenSize;
    }

    public HallReservation(HallReservationEntity hallReservationEntity) {
        this(
                hallReservationEntity.getHallReservationId(),
                hallReservationEntity.getValidUntil().toInstant(),
                hallReservationEntity.isPaidAdvance(),
                hallReservationEntity.isPaidTotal(),
                hallReservationEntity.getAdvancePrice().doubleValue(),
                hallReservationEntity.getTotalPrice().doubleValue(),
                hallReservationEntity.getReservationDate().toInstant(),
                hallReservationEntity.getScreenSize());
    }

    public int getHallReservationId() {
        return hallReservationId;
    }

    public Instant getValidUntil() {
        return validUntil;
    }

    public boolean isPaidAdvance() {
        return isPaidAdvance;
    }

    public boolean isPaidTotal() {
        return isPaidTotal;
    }

    public double getAdvancePrice() {
        return advancePrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Instant getReservationDate() {
        return reservationDate;
    }

    public int getScreenSize() {
        return screenSize;
    }
}

