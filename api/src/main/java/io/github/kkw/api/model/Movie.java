package io.github.kkw.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.time.Instant;

public class Movie {
    private final int movieId;
    private final String name;
    private final Instant startDate;
    private final Instant endDate;
    private final double basePrice;
    private final int hallId;

    public Movie(
            @JsonProperty("movieId") int movieId,
            @JsonProperty("name") String name,
            @JsonProperty("startDate") Timestamp startDate,
            @JsonProperty("endDate") Timestamp endDate,
            @JsonProperty("basePrice") double basePrice,
            @JsonProperty("hallId") int hallId) {
        this.movieId=movieId;
        this.name=name;
        this.startDate=startDate.toInstant();
        this.endDate=endDate.toInstant();
        this.basePrice=basePrice;
        this.hallId=hallId;
    }

    public int getMovieId() {
        return movieId;
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
