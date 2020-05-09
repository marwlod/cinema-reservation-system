package io.github.kkw.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.kkw.api.db.dto.HallEntity;

public class Hall {
    private final int hallId;
    private final double advancePrice;
    private final double totalPrice;
    private final double screenSize;
    private final int regularSeats;
    private final int vipSeats;

    public Hall(
            @JsonProperty("hallId") int hallId,
            @JsonProperty("advancePrice") double advancePrice,
            @JsonProperty("totalPrice") double totalPrice,
            @JsonProperty("screenSize") double screenSize,
            @JsonProperty("regularSeats") int regularSeats,
            @JsonProperty("vipSeats") int vipSeats){
        this.hallId=hallId;
        this.advancePrice=advancePrice;
        this.totalPrice=totalPrice;
        this.screenSize=screenSize;
        this.regularSeats=regularSeats;
        this.vipSeats=vipSeats;
    }

    public Hall(final HallEntity hallEntity){
        this(
                hallEntity.getHallId(),
                hallEntity.getAdvancePrice().doubleValue(),
                hallEntity.getTotalPrice().doubleValue(),
                hallEntity.getScreenSize().doubleValue(),
                hallEntity.getRegularSeats() == null ? 0 : hallEntity.getRegularSeats().intValue(),
                hallEntity.getVipSeats() == null ? 0 : hallEntity.getVipSeats().intValue());
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

    public int getRegularSeats() {
        return regularSeats;
    }

    public int getVipSeats() {
        return vipSeats;
    }
}
