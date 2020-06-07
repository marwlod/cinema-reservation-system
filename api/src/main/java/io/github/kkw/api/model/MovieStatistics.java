package io.github.kkw.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class MovieStatistics {

    private final int showCount;
    private final int totalReservations;
    private final Instant fromDate;
    private final Instant toDate;
    private final double incomeGenerated;
    private final int deletedReservations;

    public MovieStatistics(
            @JsonProperty("showCount") final int showCount,
            @JsonProperty("totalReservations") final int totalReservations,
            @JsonProperty("fromDate") final Instant fromDate,
            @JsonProperty("toDate") final Instant toDate,
            @JsonProperty("incomeGenerated") final double incomeGenerated,
            @JsonProperty("deletedReservations") final int deletedReservations){
        this.showCount=showCount;
        this.totalReservations=totalReservations;
        this.fromDate=fromDate;
        this.toDate=toDate;
        this.incomeGenerated=incomeGenerated;
        this.deletedReservations=deletedReservations;
    }

    public int getShowCount() {
        return showCount;
    }

    public int getTotalReservations() {
        return totalReservations;
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
