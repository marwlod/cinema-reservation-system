package io.github.kkw.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.kkw.api.db.dto.StatisticsEntity;

public class Statistics {

    private final int seatReservations;
    private final int hallReservations;
    private final int movies;
    private final double moneyEarned;
    private final int newClientsRegistered;
    private final int totalClientsAtTheMoment;
    private final int clientsThatReserved;

    public Statistics(
            @JsonProperty("seatReservations") int seatReservations,
            @JsonProperty("hallReservations") int hallReservations,
            @JsonProperty("movies") int movies,
            @JsonProperty("moneyEarned") double moneyEarned,
            @JsonProperty("newClientsRegistered") int newClientsRegistered,
            @JsonProperty("totalClientsAtTheMoment") int totalClientsAtTheMoment,
            @JsonProperty("clientsThatReserved") int clientsThatReserved
    ){
        this.seatReservations=seatReservations;
        this.hallReservations=hallReservations;
        this.movies=movies;
        this.moneyEarned=moneyEarned;
        this.newClientsRegistered=newClientsRegistered;
        this.totalClientsAtTheMoment=totalClientsAtTheMoment;
        this.clientsThatReserved=clientsThatReserved;
    }

    public Statistics(final StatisticsEntity statistics){
        this(statistics.getSeatReservations(),
                statistics.getHallReservations(),
                statistics.getMovies(),
                statistics.getMoneyEarned().doubleValue(),
                statistics.getNewClientsRegistered(),
                statistics.getTotalClientsAtTheMoment(),
                statistics.getClientsThatReserved());
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

    public double getMoneyEarned() {
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
}
