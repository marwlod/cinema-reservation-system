package io.github.kkw.api.db.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
public class MovieEntity {
    @Id
    private int movieId;
    private String name;
    private Timestamp startDate;
    private Timestamp endDate;
    private BigDecimal basePrice;
    private int hallId;

    public MovieEntity() {
        this.movieId = 0;
        this.name = null;
        this.startDate = null;
        this.endDate = null;
        this.basePrice = null;
        this.hallId = 0;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getName() {
        return name;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public int getHallId() {
        return hallId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public void setHallId(int hallId) {
        this.hallId = hallId;
    }
}
