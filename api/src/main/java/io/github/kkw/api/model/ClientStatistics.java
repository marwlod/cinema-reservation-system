package io.github.kkw.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientStatistics {

    private final int totalReservations;
    private final double incomeGenerated;
    private final int deletedReservations;
    private final double moneySaved;

    public ClientStatistics(
            @JsonProperty("totalReservations") final int totalReservations,
            @JsonProperty("incomeGenerated") final double incomeGenerated,
            @JsonProperty("deletedReservations") final int deletedReservations,
            @JsonProperty("moneySaved") final double moneySaved){
        this.totalReservations=totalReservations;
        this.incomeGenerated=incomeGenerated;
        this.deletedReservations=deletedReservations;
        this.moneySaved=moneySaved;
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

    public double getMoneySaved() {
        return moneySaved;
    }
}
