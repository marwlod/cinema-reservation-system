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
    private String description;
    private String link;

    public MovieEntity() {
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

    public String getDescription() { return description; }

    public String getLink() { return link; }

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

    public void setDescription(String description) {this.description = description; }

    public void setLink(String link) { this.link = link; }
}
