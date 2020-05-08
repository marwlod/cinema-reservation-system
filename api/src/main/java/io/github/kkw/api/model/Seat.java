package io.github.kkw.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.kkw.api.db.dto.SeatEntity;

public class Seat {
    private final int seatId;
    private final int hallId;
    private final int rowNo;
    private final int seatNo;
    private final boolean isVip;
    private final double basePrice;

    public Seat(@JsonProperty("seatId") int seatId,
                @JsonProperty("hallId") int hallId,
                @JsonProperty("rowNo") int rowNo,
                @JsonProperty("seatNo") int seatNo,
                @JsonProperty("isVip") boolean isVip,
                @JsonProperty("basePrice") double basePrice) {
        this.seatId = seatId;
        this.hallId = hallId;
        this.rowNo = rowNo;
        this.seatNo = seatNo;
        this.isVip = isVip;
        this.basePrice = basePrice;
    }

    public Seat(final SeatEntity seatEntity) {
        this(seatEntity.getSeatId(),
                seatEntity.getHallId(),
                seatEntity.getRowNo(),
                seatEntity.getSeatNo(),
                seatEntity.isVip(),
                seatEntity.getBasePrice().doubleValue());
    }

    public int getSeatId() {
        return seatId;
    }

    public int getHallId() {
        return hallId;
    }

    public int getRowNo() {
        return rowNo;
    }

    public int getSeatNo() {
        return seatNo;
    }

    public boolean isVip() {
        return isVip;
    }

    public double getBasePrice() {
        return basePrice;
    }
}
