package io.github.kkw.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.kkw.api.db.dto.MovieEntity;

import java.time.Instant;

public class Movie {
    private final int movieId;
    private final String name;
    private final Instant startDate;
    private final Instant endDate;
    private final double basePrice;
    private final int hallId;
    private final String description;
    private final String link;

    public Movie(
            @JsonProperty("movieId") int movieId,
            @JsonProperty("name") String name,
            @JsonProperty("startDate") Instant startDate,
            @JsonProperty("endDate") Instant endDate,
            @JsonProperty("basePrice") double basePrice,
            @JsonProperty("hallId") int hallId,
            @JsonProperty("description") String description,
            @JsonProperty("link") String link) {
        this.movieId=movieId;
        this.name=name;
        this.startDate=startDate;
        this.endDate=endDate;
        this.basePrice=basePrice;
        this.hallId=hallId;
        this.description=description;
        this.link=link;
    }

    public Movie(final MovieEntity movie) {
        this.movieId=movie.getMovieId();
        this.name=movie.getName();
        this.startDate=movie.getStartDate().toInstant();
        this.endDate=movie.getEndDate().toInstant();
        this.basePrice=movie.getBasePrice().doubleValue();
        this.hallId=movie.getHallId();
        this.description=movie.getDescription();
        this.link=movie.getLink();
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

    public String getDescription() { return description; }

    public String getLink() { return link; }
}
