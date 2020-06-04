package io.github.kkw.api;

import io.github.kkw.api.exceptions.RestError;
import io.github.kkw.api.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class ReservationSystemTestInitialStatistics{
    private static final ClientId ADMIN_CLIENT_ID = new ClientId(5005);
    private static final String VALID_CLIENT_EMAIL = "bob@crs.com"; //client id: 5000
    private static final Instant PAST_DATE = Instant.parse("2015-01-01T00:00:00Z");
    private static final Instant HALL_RESERVATION_DATE = Instant.parse("2020-05-15T23:59:59Z");
    private static final String VALID_MOVIE_NAME = "American Pie 5: Naked Mile";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnStatistics_whenThereAreReservationsPastDates(){
        //estimated values
        final int seatReservations = 1;
        final int hallReservations = 1;
        final int movies = 1;
        final double moneyEarned = 531.50;
        final int newClientsRegistered = 0;
        final int totalClientsAtTheMoment = 2;
        final int clientsThatReserved = 1;
        //when
        final ResponseEntity<Statistics> response = restTemplate.exchange(
                "/showStatistics?clientId={clientId}&from={from}&to={to}", HttpMethod.GET,
                new HttpEntity<>(null, null), Statistics.class,
                ADMIN_CLIENT_ID.getClientId(),PAST_DATE, HALL_RESERVATION_DATE);
        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(seatReservations,response.getBody().getSeatReservations());
        assertEquals(hallReservations, response.getBody().getHallReservations());
        assertEquals(movies,response.getBody().getMovies());
        assertEquals(moneyEarned, response.getBody().getMoneyEarned());
        assertEquals(newClientsRegistered, response.getBody().getNewClientsRegistered());
        assertEquals(totalClientsAtTheMoment, response.getBody().getTotalClientsAtTheMoment());
        assertEquals(clientsThatReserved, response.getBody().getClientsThatReserved());
    }

    @Test
    void shouldReturnStatistics_whenThereAreReservationsFromPastDateTillNow(){
        //estimated values
        final int seatReservations = 1;
        final int hallReservations = 1;
        final int movies = 1;
        final double moneyEarned = 531.50;
        final int newClientsRegistered = 0;
        final int totalClientsAtTheMoment = 2;
        final int clientsThatReserved = 1;
        //when
        final ResponseEntity<Statistics> response = restTemplate.exchange(
                "/showStatistics?clientId={clientId}&from={from}&to={to}", HttpMethod.GET,
                new HttpEntity<>(null, null), Statistics.class,
                ADMIN_CLIENT_ID.getClientId(),PAST_DATE, Instant.now());
        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(seatReservations,response.getBody().getSeatReservations());
        assertEquals(hallReservations, response.getBody().getHallReservations());
        assertEquals(movies,response.getBody().getMovies());
        assertEquals(moneyEarned, response.getBody().getMoneyEarned());
        assertEquals(newClientsRegistered, response.getBody().getNewClientsRegistered());
        assertEquals(totalClientsAtTheMoment, response.getBody().getTotalClientsAtTheMoment());
        assertEquals(clientsThatReserved, response.getBody().getClientsThatReserved());
    }

    @Test
    void shouldReturnStatisticForMovie_whenValidNamePastReservations(){
        //estimated values
        final int showCount = 1;
        final int totalReservations = 1;
        final Instant fromDate = Instant.parse("2020-05-15T20:00:00Z");
        final Instant toDate = Instant.parse("2020-05-15T20:00:00Z");
        final double incomeGenerated = 31.50;
        final int deletedReservations = 0;
        //when
        final ResponseEntity<MovieStatistics> response = restTemplate.exchange(
                "/showStatistics/movie/{movieName}?clientId={clientId}", HttpMethod.GET,
                new HttpEntity<>(null, null), MovieStatistics.class,
                VALID_MOVIE_NAME, ADMIN_CLIENT_ID.getClientId());
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(showCount,response.getBody().getShowCount());
        assertEquals(totalReservations, response.getBody().getTotalReservations());
        assertEquals(fromDate,response.getBody().getFromDate());
        assertEquals(toDate, response.getBody().getToDate());
        assertEquals(incomeGenerated, response.getBody().getIncomeGenerated());
        assertEquals(deletedReservations, response.getBody().getDeletedReservations());
    }

    @Test
    void shouldReturnStatisticForHallNo1_whenAdmin(){
        //given
        int hallId = 1;
        //estimated values
        final int totalReservations = 1;
        final double totalIncome = 500.00;
        final int deletedReservations = 0;
        //when
        final ResponseEntity<HallStatistics> response = restTemplate.exchange(
                "/showStatistics/hall/{hallId}?clientId={clientId}", HttpMethod.GET,
                new HttpEntity<>(null, null), HallStatistics.class,
                hallId, ADMIN_CLIENT_ID.getClientId());
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(totalReservations, response.getBody().getTotalReservations());
        assertEquals(totalIncome,response.getBody().getIncomeGenerated());
        assertEquals(deletedReservations,response.getBody().getDeletedReservations());
    }

    @Test
    void shouldNotReturnStatisticWhenNoReservations_whenAdminOneDeletedReservation(){
        //given
        int hallId = 2;
        //when
        final ResponseEntity<RestError> response = restTemplate.exchange(
                "/showStatistics/hall/{hallId}?clientId={clientId}", HttpMethod.GET,
                new HttpEntity<>(null, null), RestError.class,
                hallId, ADMIN_CLIENT_ID.getClientId());
        //then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Cannot find any reservations for hall of ID: "+hallId, response.getBody().getMessage());
    }

    @Test
    void shouldReturnStatisticsForClient_whenValidEmailAndAdmin(){
        //estimated values
        final int totalReservations = 2;
        final double incomeGenerated = 531.50;
        final int deletedReservations = 0;
        final double moneySaved = 3.50;
        //when
        final ResponseEntity<ClientStatistics> response = restTemplate.exchange(
                "/showStatistics/client/{checkedClientEmail}?clientId={clientId}", HttpMethod.GET,
                new HttpEntity<>(null,null), ClientStatistics.class,
                VALID_CLIENT_EMAIL, ADMIN_CLIENT_ID.getClientId());
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(totalReservations, response.getBody().getTotalReservations());
        assertEquals(incomeGenerated,response.getBody().getIncomeGenerated());
        assertEquals(deletedReservations,response.getBody().getDeletedReservations());
        assertEquals(moneySaved,response.getBody().getMoneySaved());
    }
}
