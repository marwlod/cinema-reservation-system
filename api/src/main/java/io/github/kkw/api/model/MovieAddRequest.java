package io.github.kkw.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class MovieAddRequest {
    private final String name;
    private final Instant startDate;
    private final Instant endDate;
    private final double basePrice;
    private final int hallId;

    public MovieAddRequest(@JsonProperty("name") final String name,
                           @JsonProperty("startDate") final Instant startDate,
                           @JsonProperty("endDate") final Instant endDate,
                           @JsonProperty("basePrice") final double basePrice,
                           @JsonProperty("hallId") final int hallId) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.basePrice = basePrice;
        this.hallId = hallId;
    }

    public String getName() {
        return name;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public int getHallId() {
        return hallId;
    }
}
