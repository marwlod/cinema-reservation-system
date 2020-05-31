package io.github.kkw.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HallStatistics {

    private final int totalReservations;
    private final double incomeGenerated;
    private final int deletedReservations;

    public HallStatistics(
            @JsonProperty("totalReservations") final int totalReservations,
            @JsonProperty("incomeGenerated") final double incomeGenerated,
            @JsonProperty("deletedReservations") final int deletedReservations){
        this.totalReservations=totalReservations;
        this.incomeGenerated=incomeGenerated;
        this.deletedReservations=deletedReservations;
    }

    public int getTotalReservations() {
        return totalReservations;
    }

    public double getIncomeGenerated() {
        return incomeGenerated;
    }

    public int getDeletedReservations() {
        return deletedReservations;
    }
}
