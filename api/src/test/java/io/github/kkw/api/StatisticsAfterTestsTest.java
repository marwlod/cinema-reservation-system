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

public class StatisticsAfterTestsTest {
    private static final int VALID_MOVIE_ID = 1;
    private static final int VALID_SEAT_ID = 102;
    private static final String VALID_CODE = "A123";
    private static final ClientId VALID_CLIENT_ID = new ClientId(5000);
    private static final ClientId ADMIN_CLIENT_ID = new ClientId(5005);
    private static final String VALID_CLIENT_EMAIL = "bob@crs.com"; //client id: 5000
    private static final String INVALID_CLIENT_EMAIL = "invalid_email@crs.com";
    private static final int VALID_HALL_ID = 1;
    private static final int INVALID_HALL_ID = 99_999_999;
    private static final Instant FUTURE_DATE_EARLIER = Instant.parse("2666-01-01T00:00:00Z");
    private static final Instant FUTURE_DATE_LATER = Instant.parse("2666-01-02T00:00:00Z");
    private static final Instant PAST_DATE = Instant.parse("2015-01-01T00:00:00Z");
    private static final Instant HALL_RESERVATION_DATE = Instant.parse("2020-05-15T23:59:59Z");
    private static final Instant PAST_DATE_EARLIER = Instant.parse("2015-01-01T00:00:00Z");
    private static final Instant PAST_DATE_LATER = Instant.parse("2020-05-15T23:59:59Z");
    private static final Instant NO_HALL_RESERVATION_DATE = Instant.parse("2020-01-01T00:00:00Z");
    private static final String VALID_MOVIE_NAME_NO_RESERVATIONS = "Joker";
    private static final String VALID_MOVIE_NAME = "American Pie 5: Naked Mile";
    private static final String INVALID_MOVIE_NAME = "INVALID MOVIE NAME";

    @Autowired
    private TestRestTemplate restTemplate;

    private ReservationId reserveSeat() {
        return reserveSeat(VALID_SEAT_ID);
    }

    private ReservationId reserveSeat(int seatId) {
        final ResponseEntity<ReservationId> reservationResponse = restTemplate.exchange(
                "/reserveSeat/{movieId}/{seatId}?clientId={clientId}&code={code}", HttpMethod.POST,
                new HttpEntity<>(null, null), ReservationId.class,
                VALID_MOVIE_ID, seatId, VALID_CLIENT_ID.getClientId(),VALID_CODE);
        assertEquals(HttpStatus.OK, reservationResponse.getStatusCode());
        assertNotNull(reservationResponse.getBody());
        return reservationResponse.getBody();
    }

    @Test
    void shouldNotReturnStatistics_whenClientIsNotAdmin(){
        //when
        final ResponseEntity<RestError> response = restTemplate.exchange(
                "/showStatistics?clientId={clientId}&from={from}&to={to}", HttpMethod.GET,
                new HttpEntity<>(null, null), RestError.class,
                VALID_CLIENT_ID.getClientId(),HALL_RESERVATION_DATE, HALL_RESERVATION_DATE);

        // then
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Only admin can do this", response.getBody().getMessage());
    }

    @Test
    void shouldNotReturnStatistics_whenFutureDates(){
        //when
        final ResponseEntity<RestError> response = restTemplate.exchange(
                "/showStatistics?clientId={clientId}&from={from}&to={to}", HttpMethod.GET,
                new HttpEntity<>(null, null), RestError.class,
                ADMIN_CLIENT_ID.getClientId(),FUTURE_DATE_EARLIER, FUTURE_DATE_LATER);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Dates from the future", response.getBody().getMessage());
    }

    @Test
    void shouldNotReturnStatistics_whenWrongPastDates(){
        //when
        final ResponseEntity<RestError> response = restTemplate.exchange(
                "/showStatistics?clientId={clientId}&from={from}&to={to}", HttpMethod.GET,
                new HttpEntity<>(null, null), RestError.class,
                ADMIN_CLIENT_ID.getClientId(),PAST_DATE_LATER, PAST_DATE_EARLIER);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("From date cannot be after To date", response.getBody().getMessage());
    }

    @Test
    void shouldReturnEmptyStatistics_whenNoReservationThisDate(){
        //when
        final ResponseEntity<Statistics> response = restTemplate.exchange(
                "/showStatistics?clientId={clientId}&from={from}&to={to}", HttpMethod.GET,
                new HttpEntity<>(null, null), Statistics.class,
                ADMIN_CLIENT_ID.getClientId(),NO_HALL_RESERVATION_DATE, NO_HALL_RESERVATION_DATE);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void shouldReturnStatistics_whenThereAreReservationsPastDates(){
        //estimated values
        final int seatReservations = 1;
        final int hallReservations = 1;
        final int movies = 1;
        final double moneyEarned = 531.50;
        final int newClientsRegistered = 0;
        final int totalClientsAtTheMoment = 3;
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
        final int seatReservations = 16;
        final int hallReservations = 13;
        final int movies = 1;
        final double moneyEarned = 1031.50;
        final int newClientsRegistered = 1;
        final int totalClientsAtTheMoment = 3;
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
    void shouldNotReturnStatisticForMovie_whenValidNameButNotReservations(){
        //when
        final ResponseEntity<RestError> response = restTemplate.exchange(
                "/showStatistics/movie/{movieName}?clientId={clientId}", HttpMethod.GET,
                new HttpEntity<>(null, null), RestError.class,
                VALID_MOVIE_NAME_NO_RESERVATIONS, ADMIN_CLIENT_ID.getClientId());
        //then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Cannot find any reservations for movie named: "+VALID_MOVIE_NAME_NO_RESERVATIONS, response.getBody().getMessage());
    }

    @Test
    void shouldNotReturnStatisticForMovie_whenInvalidName(){
        //when
        final ResponseEntity<RestError> response = restTemplate.exchange(
                "/showStatistics/movie/{movieName}?clientId={clientId}", HttpMethod.GET,
                new HttpEntity<>(null, null), RestError.class,
                INVALID_MOVIE_NAME, ADMIN_CLIENT_ID.getClientId());
        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("There is not any movie named: "+INVALID_MOVIE_NAME, response.getBody().getMessage());
    }

    @Test
    void shouldNotReturnStatisticForMovie_whenNotAdmin(){
        //when
        final ResponseEntity<RestError> response = restTemplate.exchange(
                "/showStatistics/movie/{movieName}?clientId={clientId}", HttpMethod.GET,
                new HttpEntity<>(null, null), RestError.class,
                VALID_MOVIE_NAME, VALID_CLIENT_ID.getClientId());
        //then
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Only admin can do this", response.getBody().getMessage());
    }

    @Test
    void shouldReturnStatisticForHallNo1_whenAdmin(){
        //given
        int hallId = 1;
        //estimated values
        final int totalReservations = 12;
        final double totalIncome = 1000.00;
        final int deletedReservations = 11;
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
    void shouldReturnStatisticForHallNo2_whenAdminOneDeletedReservation(){
        //given
        int hallId = 2;
        //estimated values
        final int totalReservations = 1;
        final double totalIncome = 0.00;
        final int deletedReservations = 1;
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
    void shouldNotReturnStatisticForHall_whenHallDoesntExist(){
        //when
        final ResponseEntity<RestError> response = restTemplate.exchange(
                "/showStatistics/hall/{hallId}?clientId={clientId}", HttpMethod.GET,
                new HttpEntity<>(null, null), RestError.class,
                INVALID_HALL_ID, ADMIN_CLIENT_ID.getClientId());
        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Hall "+ INVALID_HALL_ID +" doesn't exist", response.getBody().getMessage());
    }

    @Test
    void shouldNotReturnStatisticForHall_whenNotAdmin(){
        //when
        final ResponseEntity<RestError> response = restTemplate.exchange(
                "/showStatistics/hall/{hallId}?clientId={clientId}", HttpMethod.GET,
                new HttpEntity<>(null, null), RestError.class,
                VALID_HALL_ID, VALID_CLIENT_ID.getClientId());
        //then
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Only admin can do this", response.getBody().getMessage());
    }

    @Test
    void shouldReturnStatisticsForClient_whenValidEmailAndAdmin(){
        //estimated values
        final int totalReservations = 29;
        final double incomeGenerated = 1031.50;
        final int deletedReservations = 27;
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

    @Test
    void shouldReturnStatisticsOneMoreTotalReservationsForClient_whenClientReserveSeatForFutureMovie(){
        //estimated values
        final int totalReservations = 29;
        final double incomeGenerated = 1031.50;
        final int deletedReservations = 27;
        final double moneySaved = 3.50;
        //given
        final ReservationId reservationId = reserveSeat();
        final ResponseEntity<ClientStatistics> statisticsResponse = restTemplate.exchange(
                "/showStatistics/client/{checkedClientEmail}?clientId={clientId}", HttpMethod.GET,
                new HttpEntity<>(null,null), ClientStatistics.class,
                VALID_CLIENT_EMAIL, ADMIN_CLIENT_ID.getClientId());
        //then
        assertEquals(HttpStatus.OK, statisticsResponse.getStatusCode());
        assertNotNull(statisticsResponse.getBody());
        assertEquals(totalReservations, statisticsResponse.getBody().getTotalReservations());
        assertEquals(incomeGenerated,statisticsResponse.getBody().getIncomeGenerated());
        assertEquals(deletedReservations,statisticsResponse.getBody().getDeletedReservations());
        assertEquals(moneySaved,statisticsResponse.getBody().getMoneySaved());
        final ResponseEntity<Void> deleteSeatReservationResponse = restTemplate.exchange(
                "/reserveSeat/{reservationId}?clientId={clientId}", HttpMethod.DELETE,
                new HttpEntity<>(null, null), Void.class,
                reservationId.getReservationId(), VALID_CLIENT_ID.getClientId());

        // then
        assertEquals(HttpStatus.OK, deleteSeatReservationResponse.getStatusCode());
    }

    @Test
    void shouldReturnOneMoreTotalDeletedReservationForClient_whenClientReserveSeatForFutureMovie(){
        //estimated values
        final int totalReservations = 29;
        final double incomeGenerated = 1031.50;
        final int deletedReservations = 27;
        final double moneySaved = 3.50;
        //when
        final ResponseEntity<ClientStatistics> statisticsResponse = restTemplate.exchange(
                "/showStatistics/client/{checkedClientEmail}?clientId={clientId}", HttpMethod.GET,
                new HttpEntity<>(null,null), ClientStatistics.class,
                VALID_CLIENT_EMAIL, ADMIN_CLIENT_ID.getClientId());
        //then
        assertEquals(HttpStatus.OK, statisticsResponse.getStatusCode());
        assertNotNull(statisticsResponse.getBody());
        assertEquals(totalReservations, statisticsResponse.getBody().getTotalReservations());
        assertEquals(incomeGenerated,statisticsResponse.getBody().getIncomeGenerated());
        assertEquals(deletedReservations,statisticsResponse.getBody().getDeletedReservations());
        assertEquals(moneySaved,statisticsResponse.getBody().getMoneySaved());
    }


    @Test
    void shouldNotReturnStatisticsForClient_whenNotAdmin(){
        //when
        final ResponseEntity<RestError> response = restTemplate.exchange(
                "/showStatistics/client/{clientEmail}?clientId={clientId}", HttpMethod.GET,
                new HttpEntity<>(null,null), RestError.class,
                VALID_CLIENT_EMAIL, VALID_CLIENT_ID.getClientId());
        //then
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Only admin can do this", response.getBody().getMessage());
    }

    @Test
    void shouldNotReturnStatisticsForClient_whenInvalidClientEmail(){
        //when
        final ResponseEntity<RestError> response = restTemplate.exchange(
                "/showStatistics/client/{clientEmail}?clientId={clientId}", HttpMethod.GET,
                new HttpEntity<>(null,null), RestError.class,
                INVALID_CLIENT_EMAIL, ADMIN_CLIENT_ID.getClientId());
        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Cannot find Client with email: "+INVALID_CLIENT_EMAIL, response.getBody().getMessage());
    }

}


