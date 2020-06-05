package io.github.kkw.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.kkw.api.db.dto.SeatReservationEntity;

import java.time.Instant;

public class SeatReservation {
    private final int seatReservationId;
    private final Instant validUntil;
    private final boolean isPaid;
    private final double totalPrice;
    private final String movieName;
    private final Instant startDate;
    private final Instant endDate;
    private final int hallId;
    private final int rowNumber;
    private final int seatNumber;
    private final boolean isVip;
    private final boolean isDeleted;

    public SeatReservation(@JsonProperty("seatReservationId") int seatReservationId,
                           @JsonProperty("validUntil") Instant validUntil,
                           @JsonProperty("isPaid") boolean isPaid,
                           @JsonProperty("totalPrice") double totalPrice,
                           @JsonProperty("movieName") String movieName,
                           @JsonProperty("startDate") Instant startDate,
                           @JsonProperty("endDate") Instant endDate,
                           @JsonProperty("hallId") int hallId,
                           @JsonProperty("rowNumber") int rowNumber,
                           @JsonProperty("seatNumber") int seatNumber,
                           @JsonProperty("isVip") boolean isVip,
                           @JsonProperty("isDeleted") boolean isDeleted) {
        this.seatReservationId = seatReservationId;
        this.validUntil = validUntil;
        this.isPaid = isPaid;
        this.totalPrice = totalPrice;
        this.movieName = movieName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hallId = hallId;
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.isVip = isVip;
        this.isDeleted = isDeleted;
    }

    public SeatReservation(SeatReservationEntity seatReservationEntity) {
        this(
                seatReservationEntity.getSeatReservationId(),
                seatReservationEntity.getValidUntil().toInstant(),
                seatReservationEntity.isPaid(),
                seatReservationEntity.getTotalPrice().doubleValue(),
                seatReservationEntity.getMovieName(),
                seatReservationEntity.getStartDate().toInstant(),
                seatReservationEntity.getEndDate().toInstant(),
                seatReservationEntity.getHallId(),
                seatReservationEntity.getRowNo(),
                seatReservationEntity.getSeatNo(),
                seatReservationEntity.isVip(),
                seatReservationEntity.isDeleted());
    }

    public int getSeatReservationId() {
        return seatReservationId;
    }

    public Instant getValidUntil() {
        return validUntil;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getMovieName() {
        return movieName;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public int getHallId() {
        return hallId;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public boolean isVip() {
        return isVip;
    }

    public boolean isDeleted() {
        return isDeleted;
    }
}

