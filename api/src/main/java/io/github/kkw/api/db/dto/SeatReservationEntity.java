package io.github.kkw.api.db.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
public class SeatReservationEntity {
    @Id
    private int seatReservationId;
    private Timestamp validUntil;
    private boolean isPaid;
    private BigDecimal totalPrice;
    private String movieName;
    private Timestamp startDate;
    private Timestamp endDate;
    private int hallId;
    private int rowNo;
    private int seatNo;
    private boolean isVip;

    public int getSeatReservationId() {
        return seatReservationId;
    }

    public Timestamp getValidUntil() {
        return validUntil;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public String getMovieName() {
        return movieName;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
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

    public void setSeatReservationId(int seatReservationId) {
        this.seatReservationId = seatReservationId;
    }

    public void setValidUntil(Timestamp validUntil) {
        this.validUntil = validUntil;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public void setHallId(int hallId) {
        this.hallId = hallId;
    }

    public void setRowNo(int rowNumber) {
        this.rowNo = rowNumber;
    }

    public void setSeatNo(int seatNumber) {
        this.seatNo = seatNumber;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }
}
