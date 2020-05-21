package io.github.kkw.api.db.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class StatisticsEntity {

    @Id
    private int seatReservations;
    private int hallReservations;
    private int movies;
    private BigDecimal moneyEarned;
    private int newClientsRegistered;
    private int totalClientsAtTheMoment;
    private int clientsThatReserved;

    public StatisticsEntity(){

    }

    public int getSeatReservations() {
        return seatReservations;
    }

    public int getHallReservations() {
        return hallReservations;
    }

    public int getMovies() {
        return movies;
    }

    public BigDecimal getMoneyEarned() {
        return moneyEarned;
    }

    public int getNewClientsRegistered() {
        return newClientsRegistered;
    }

    public int getTotalClientsAtTheMoment() {
        return totalClientsAtTheMoment;
    }

    public int getClientsThatReserved() {
        return clientsThatReserved;
    }

    public void setSeatReservations(int seatReservations) {
        this.seatReservations = seatReservations;
    }

    public void setHallReservations(int hallReservations) {
        this.hallReservations = hallReservations;
    }

    public void setMovies(int movies) {
        this.movies = movies;
    }

    public void setMoneyEarned(BigDecimal moneyEarned) {
        this.moneyEarned = moneyEarned;
    }

    public void setNewClientsRegistered(int newClientsRegistered) {
        this.newClientsRegistered = newClientsRegistered;
    }

    public void setTotalClientsAtTheMoment(int totalClientsAtTheMoment) {
        this.totalClientsAtTheMoment = totalClientsAtTheMoment;
    }

    public void setClientsThatReserved(int clientsThatReserved) {
        this.clientsThatReserved = clientsThatReserved;
    }
}
