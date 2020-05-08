package io.github.kkw.api.db.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class SeatEntity {
    @Id
    private int seatId;
    private int hallId;
    private int rowNo;
    private int seatNo;
    private boolean isVip;
    private BigDecimal basePrice;

    public SeatEntity() {
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

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public void setHallId(int hallId) {
        this.hallId = hallId;
    }

    public void setRowNo(int rowNo) {
        this.rowNo = rowNo;
    }

    public void setSeatNo(int seatNo) {
        this.seatNo = seatNo;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }
}
