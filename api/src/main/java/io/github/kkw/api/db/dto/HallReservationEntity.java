package io.github.kkw.api.db.dto;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
public class HallReservationEntity {
    @Id
    private int hallReservationId;
    private Timestamp validUntil;
    private boolean isPaidAdvance;
    private boolean isPaidTotal;
    private Timestamp reservationDate;
    private BigDecimal advancePrice;
    private BigDecimal totalPrice;
    private int screenSize;

    public int getHallReservationId() {
        return hallReservationId;
    }

    public Timestamp getValidUntil() {
        return validUntil;
    }

    public boolean isPaidAdvance() {
        return isPaidAdvance;
    }

    public boolean isPaidTotal() {
        return isPaidTotal;
    }

    public Timestamp getReservationDate() {
        return reservationDate;
    }

    public BigDecimal getAdvancePrice() {
        return advancePrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public int getScreenSize() {
        return screenSize;
    }

    public void setHallReservationId(int hallReservationId) {
        this.hallReservationId = hallReservationId;
    }

    public void setValidUntil(Timestamp validUntil) {
        this.validUntil = validUntil;
    }

    public void setPaidAdvance(boolean paidAdvance) {
        isPaidAdvance = paidAdvance;
    }

    public void setPaidTotal(boolean paidTotal) {
        isPaidTotal = paidTotal;
    }

    public void setReservationDate(Timestamp reservationDate) {
        this.reservationDate = reservationDate;
    }

    public void setAdvancePrice(BigDecimal advancePrice) {
        this.advancePrice = advancePrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setScreenSize(int screenSize) {
        this.screenSize = screenSize;
    }
}
