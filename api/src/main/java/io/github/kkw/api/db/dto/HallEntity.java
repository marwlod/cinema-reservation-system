package io.github.kkw.api.db.dto;

import java.math.BigDecimal;

public class HallEntity {

    private int hallId;
    private BigDecimal advancePrice;
    private BigDecimal totalPrice;
    private BigDecimal screenSize;

    public HallEntity(){
        this.hallId = 0;
        this.advancePrice = null;
        this.totalPrice = null;
        this.screenSize = null;
    }

    public int getHallId() {
        return hallId;
    }

    public BigDecimal getAdvancePrice() {
        return advancePrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public BigDecimal getScreenSize() {
        return screenSize;
    }

    public void setHallId(int hallId) {
        this.hallId = hallId;
    }

    public void setAdvancePrice(BigDecimal advancePrice) {
        this.advancePrice = advancePrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setScreenSize(BigDecimal screenSize) {
        this.screenSize = screenSize;
    }
}
