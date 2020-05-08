package io.github.kkw.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.kkw.api.db.dto.HallEntity;

public class Hall {

    private int hallId;
    private double advancePrice;
    private double totalPrice;
    private double screenSize;

    public Hall(
            @JsonProperty("hallId") int hallId,
            @JsonProperty("advancePrice") double advancePrice,
            @JsonProperty("totalPrice") double totalPrice,
            @JsonProperty("screenSize") double screenSize){
        this.hallId=hallId;
        this.advancePrice=advancePrice;
        this.totalPrice=totalPrice;
        this.screenSize=screenSize;
    }

    public Hall(final HallEntity hallEntity){
        this(
                hallEntity.getHallId(),
                hallEntity.getAdvancePrice().doubleValue(),
                hallEntity.getTotalPrice().doubleValue(),
                hallEntity.getScreenSize().doubleValue());
    }

    public int getHallId() {
        return hallId;
    }

    public double getAdvancePrice() {
        return advancePrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getScreenSize() {
        return screenSize;
    }
}
