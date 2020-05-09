package io.github.kkw.api.db.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
public class HallEntity {
    @Id
    private int hallId;
    private BigDecimal advancePrice;
    private BigDecimal totalPrice;
    private BigDecimal screenSize;
    private BigInteger regularSeats;
    private BigInteger vipSeats;

    public HallEntity(){
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

    public BigInteger getRegularSeats() {
        return regularSeats;
    }

    public BigInteger getVipSeats() {
        return vipSeats;
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

    public void setRegularSeats(BigInteger regularSeats) {
        this.regularSeats = regularSeats;
    }

    public void setVipSeats(BigInteger vipSeats) {
        this.vipSeats = vipSeats;
    }
}
