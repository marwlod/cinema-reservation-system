package io.github.kkw.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class MovieStatistics {

    private final int showTimeCount;
    private final int reservations;
    private final Instant fromDate;
    private final Instant toDate;
    private final double incomeGenerated;
    private final int deletedReservations;

    public MovieStatistics(
            @JsonProperty("showTimeCount") final int showTimeCount,
            @JsonProperty("reservations") final int reservations,
            @JsonProperty("fromDate") final Instant fromDate,
            @JsonProperty("toDate") final Instant toDate,
            @JsonProperty("incomeGenerated") final double incomeGenerated,
            @JsonProperty("deletedReservations") final int deletedReservations){
        this.showTimeCount=showTimeCount;
        this.reservations=reservations;
        this.fromDate=fromDate;
        this.toDate=toDate;
        this.incomeGenerated=incomeGenerated;
        this.deletedReservations=deletedReservations;
    }

    public int getShowTimeCount() {
        return showTimeCount;
    }

    public int getReservations() {
        return reservations;
    }

    public Instant getFromDate() {
        return fromDate;
    }

    public Instant getToDate() {
        return toDate;
    }

    public double getIncomeGenerated() {
        return incomeGenerated;
    }

    public int getDeletedReservations() {
        return deletedReservations;
    }
}
