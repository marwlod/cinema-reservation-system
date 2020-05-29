package io.github.kkw.api;

import io.github.kkw.api.exceptions.RestError;
import io.github.kkw.api.model.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReservationsSystemTest {
    // they need to correspond to the real values in the db (see insert_data.sql)
    private static final int VALID_MOVIE_ID = 1;
    private static final int INVALID_MOVIE_ID = 99_999_999;
    private static final int VALID_SEAT_ID = 101;
    private static final int INVALID_SEAT_ID = 99_999_999;
    private static final ClientId VALID_CLIENT_ID = new ClientId(5000);
    private static final ClientId ADMIN_CLIENT_ID = new ClientId(5005);
    private static final ReservationId INVALID_RESERVATION_ID = new ReservationId(99_999_999);
    private static final int VALID_HALL_ID = 1;
    private static final int INVALID_HALL_ID = 99_999_999;
    private static final MovieAddRequest MOVIE_ADD_REQUEST = new MovieAddRequest("Fast and Furious",
            Instant.parse("2100-01-01T10:00:30.00Z"), Instant.parse("2100-01-01T11:30:30.00Z"), 25.00, VALID_HALL_ID);
    private static final Instant NO_MOVIES_FROM = Instant.parse("2666-01-01T00:00:00Z");
    private static final Instant NO_MOVIES_UP_TO = Instant.parse("2666-01-02T00:00:00Z");
    private static final Instant MOVIES_FROM = Instant.parse("2030-01-01T00:00:00Z");
    private static final Instant MOVIES_UP_TO = Instant.parse("2030-01-02T00:00:00Z");
    private static final Instant FREE_DATE = Instant.parse("2025-01-01T00:00:00Z");
    private static final Instant PAST_DATE = Instant.parse("2015-01-01T00:00:00Z");
    private static final Instant HALL_RESERVATION_DATE = Instant.parse("2020-05-15T23:59:59Z");
    private static final Instant NO_HALL_RESERVATION_DATE = Instant.parse("2020-01-01T00:00:00Z");
    private static final double SEAT_COST = 20;
    private static final double HALL_ADVANCE_COST = 50;
    private static final double HALL_COST = 500;
    //TODO hardcoded password to connect with payments mock, ugly :(
    private static final String PAYMENTS_MOCK_PASSWORD = "supersecretTO2";
    private static final String VALID_CODE = "A123";
    private static final String INVALID_CODE = "INVALID_CODE_098";
    private static final SpecialOfferAddRequest existingCodeSpecialOffer = new SpecialOfferAddRequest(VALID_CODE, 50);

    @Autowired
    private TestRestTemplate restTemplate;

    @Nested
    class SeatReservationTest {
        @Test
        void shouldReserveSeat_whenSeatFreeToReserveValidCode() {
            // when
            final ResponseEntity<ReservationId> response = restTemplate.exchange(
                    "/reserveSeat/{movieId}/{seatId}?clientId={clientId}&code={code}", HttpMethod.POST,
                    new HttpEntity<>(null, null), ReservationId.class,
                    VALID_MOVIE_ID, VALID_SEAT_ID, VALID_CLIENT_ID.getClientId(), VALID_CODE);

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            cleanUpSeatReservation(response.getBody());
        }

        @Test
        void shouldReserveSeat_whenSeatFreeToReserveInvalidCode() {
            // when
            final ResponseEntity<ReservationId> response = restTemplate.exchange(
                    "/reserveSeat/{movieId}/{seatId}?clientId={clientId}&code={code}", HttpMethod.POST,
                    new HttpEntity<>(null, null), ReservationId.class,
                    VALID_MOVIE_ID, VALID_SEAT_ID, VALID_CLIENT_ID.getClientId(), INVALID_CODE);

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            cleanUpSeatReservation(response.getBody());
        }

        @Test
        void shouldReserveSeat_whenSeatFreeToReserveWithoutCode() {
            // when
            final ResponseEntity<ReservationId> response = restTemplate.exchange(
                    "/reserveSeat/{movieId}/{seatId}?clientId={clientId}&code={code}", HttpMethod.POST,
                    new HttpEntity<>(null, null), ReservationId.class,
                    VALID_MOVIE_ID, VALID_SEAT_ID, VALID_CLIENT_ID.getClientId(),null);

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            cleanUpSeatReservation(response.getBody());
        }

        @Test
        void shouldNotReserveSeat_whenClientNotLoggedIn() {
            // given
            final ClientId clientId = registerClientAndLogout();

            // when
            final ResponseEntity<RestError> response = restTemplate.exchange(
                    "/reserveSeat/{movieId}/{seatId}?clientId={clientId}&code={code}", HttpMethod.POST,
                    new HttpEntity<>(null, null), RestError.class,
                    VALID_MOVIE_ID, VALID_SEAT_ID, clientId.getClientId(), VALID_CODE);

            // then
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Client doesn't exist or not logged in", response.getBody().getMessage());
        }

        private ClientId registerClientAndLogout() {
            // register
            final String validEmail = new Random().nextLong() + "@domain.com";
            final String password = "abcd1234";
            final ResponseEntity<ClientId> registerResponse = restTemplate.exchange(
                    "/register?email={email}&password={password}", HttpMethod.POST,
                    new HttpEntity<>(null, null), ClientId.class,
                    validEmail, password);
            assertEquals(HttpStatus.OK, registerResponse.getStatusCode());
            assertNotNull(registerResponse.getBody());

            // logout
            final ResponseEntity<Void> logoutResponse = restTemplate.exchange(
                    "/logout?clientId={clientId}", HttpMethod.POST,
                    new HttpEntity<>(null, null), Void.class,
                    registerResponse.getBody().getClientId());
            assertEquals(HttpStatus.OK, logoutResponse.getStatusCode());

            return registerResponse.getBody();
        }

        @Test
        void shouldNotReserveSeat_whenMovieNotFound() {
            // when
            final ResponseEntity<RestError> response = restTemplate.exchange(
                    "/reserveSeat/{movieId}/{seatId}?clientId={clientId}&code={code}", HttpMethod.POST,
                    new HttpEntity<>(null, null), RestError.class,
                    INVALID_MOVIE_ID, VALID_SEAT_ID, VALID_CLIENT_ID.getClientId(),VALID_CODE);

            // then
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Movie with this ID not found", response.getBody().getMessage());
        }

        @Test
        void shouldNotReserveSeat_whenSeatNotFound() {
            // when
            final ResponseEntity<RestError> response = restTemplate.exchange(
                    "/reserveSeat/{movieId}/{seatId}?clientId={clientId}&code={code}", HttpMethod.POST,
                    new HttpEntity<>(null, null), RestError.class,
                    VALID_MOVIE_ID, INVALID_SEAT_ID, VALID_CLIENT_ID.getClientId(),VALID_CODE);

            // then
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Seat with this ID for this movie not found", response.getBody().getMessage());
        }

        @Test
        void shouldNotReserveSeat_whenAlreadyReserved() {
            // given
            final ReservationId reservationId = reserveSeat();

            // when
            final ResponseEntity<RestError> response = restTemplate.exchange(
                    "/reserveSeat/{movieId}/{seatId}?clientId={clientId}&code={code}", HttpMethod.POST,
                    new HttpEntity<>(null, null), RestError.class,
                    VALID_MOVIE_ID, VALID_SEAT_ID, VALID_CLIENT_ID.getClientId(),VALID_CODE);

            // then
            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Chosen seat for this movie is already reserved", response.getBody().getMessage());
            cleanUpSeatReservation(reservationId);
        }

        @Test
        void shouldDeleteSeatReservation_whenReservedByThisClient() {
            // given
            final ReservationId reservationId = reserveSeat();

            // when
            final ResponseEntity<Void> response = restTemplate.exchange(
                    "/reserveSeat/{reservationId}?clientId={clientId}", HttpMethod.DELETE,
                    new HttpEntity<>(null, null), Void.class,
                    reservationId.getReservationId(), VALID_CLIENT_ID.getClientId());

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldNotDeleteSeatReservation_whenReservationNotFound() {
            // when
            final ResponseEntity<RestError> response = restTemplate.exchange(
                    "/reserveSeat/{reservationId}?clientId={clientId}", HttpMethod.DELETE,
                    new HttpEntity<>(null, null), RestError.class,
                    INVALID_RESERVATION_ID.getReservationId(), VALID_CLIENT_ID.getClientId());

            // then
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Reservation with this ID not found", response.getBody().getMessage());
        }
    }

    @Nested
    class MoviesTest {
        @Test
        void shouldReturnMovies_whenMoviesScheduled() {
            // when
            final ResponseEntity<List<Movie>> response = restTemplate.exchange(
                    "/showMovies?from={from}&to={to}", HttpMethod.GET,
                    new HttpEntity<>(null, null), new ParameterizedTypeReference<List<Movie>>(){},
                    MOVIES_FROM, MOVIES_UP_TO);

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
        }

        @Test
        void shouldNotReturnAnyMovie_whenNoMoviesAreScheduled() {
            // when
            final ResponseEntity<RestError> response = restTemplate.exchange(
                    "/showMovies?from={from}&to={to}", HttpMethod.GET,
                    new HttpEntity<>(null, null), RestError.class,
                    NO_MOVIES_FROM, NO_MOVIES_UP_TO);

            // then
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Cannot find any movies in this period", response.getBody().getMessage());
        }

        @Disabled("implement delete to test this properly")
        @Test
        void shouldAddMovie_whenValidData(){
            // when
            final ResponseEntity<Void> response = restTemplate.exchange(
                    "/addMovie?clientId={clientId}", HttpMethod.POST,
                    new HttpEntity<>(MOVIE_ADD_REQUEST, null), Void.class,
                    VALID_CLIENT_ID.getClientId());

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Nested
    class HallReservationTest {
        @Test
        void shouldReserveHall_whenHallFreeToReserve() {
            // when
            final ResponseEntity<ReservationId> response = restTemplate.exchange(
                    "/reserveHall/{hallId}/{date}?clientId={clientId}", HttpMethod.POST,
                    new HttpEntity<>(null, null), ReservationId.class,
                    VALID_HALL_ID, FREE_DATE, VALID_CLIENT_ID.getClientId());

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            cleanUpHallReservation(response.getBody());
        }

        @Test
        void shouldNotReserveHall_whenHallNotFound() {
            // when
            final ResponseEntity<RestError> response = restTemplate.exchange(
                    "/reserveHall/{hallId}/{date}?clientId={clientId}", HttpMethod.POST,
                    new HttpEntity<>(null, null), RestError.class,
                    INVALID_HALL_ID, FREE_DATE, VALID_CLIENT_ID.getClientId());

            // then
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Hall with this ID not found", response.getBody().getMessage());
        }

        @Test
        void shouldNotReserveHall_whenAlreadyReserved() {
            // given
            final ReservationId reservationId = reserveHall();

            // when
            final ResponseEntity<RestError> response = restTemplate.exchange(
                    "/reserveHall/{hallId}/{date}?clientId={clientId}", HttpMethod.POST,
                    new HttpEntity<>(null, null), RestError.class,
                    VALID_HALL_ID, FREE_DATE, VALID_CLIENT_ID.getClientId());

            // then
            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Hall with this ID is already reserved for chosen day", response.getBody().getMessage());
            cleanUpHallReservation(reservationId);
        }

        @Test
        void shouldNotReserveHall_whenDateInThePast() {
            // when
            final ResponseEntity<RestError> response = restTemplate.exchange(
                    "/reserveHall/{hallId}/{date}?clientId={clientId}", HttpMethod.POST,
                    new HttpEntity<>(null, null), RestError.class,
                    VALID_HALL_ID, PAST_DATE, VALID_CLIENT_ID.getClientId());

            // then
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Date must be at least 14 days in the future", response.getBody().getMessage());
        }
    }

    @Nested
    class PaymentsTest {
        @Test
        void shouldReturnURL_whenRequestToPayForSeat() {
            // given
            final ReservationId reservationId = reserveSeat();
            final String expectedURL = buildURL(reservationId, SEAT_COST, "seat");

            // when
            final ResponseEntity<URL> response = restTemplate.exchange(
                    "/payForSeat/{reservationId}?clientId={clientId}", HttpMethod.POST,
                    new HttpEntity<>(null, null), URL.class,
                    reservationId.getReservationId(), VALID_CLIENT_ID.getClientId());

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(expectedURL, response.getBody().toString());
            cleanUpSeatReservation(reservationId);
        }

        @Test
        void shouldNotPayForSeat_whenReservationNotFound() {
            // when
            final ResponseEntity<RestError> response = restTemplate.exchange(
                    "/payForSeat/{reservationId}?clientId={clientId}", HttpMethod.POST,
                    new HttpEntity<>(null, null), RestError.class,
                    INVALID_RESERVATION_ID.getReservationId(), VALID_CLIENT_ID.getClientId());

            // then
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Reservation with this ID not found", response.getBody().getMessage());
        }

        @Test
        void shouldNotPayForSeat_whenReservationAlreadyPaid() {
            // given
            final ReservationId reservationId = reserveSeat();
            finishSeatPayment(reservationId);

            // when
            final ResponseEntity<RestError> response = restTemplate.exchange(
                    "/payForSeat/{reservationId}?clientId={clientId}", HttpMethod.POST,
                    new HttpEntity<>(null, null), RestError.class,
                    reservationId.getReservationId(), VALID_CLIENT_ID.getClientId());

            // then
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Reservation with this ID is already paid for", response.getBody().getMessage());
            cleanUpSeatReservation(reservationId);
        }

        @Test
        void shouldFinishSeatPayment_whenValidPassword() {
            // given
            final ReservationId reservationId = reserveSeat();

            // when
            final ResponseEntity<Void> response = restTemplate.exchange(
                    "/finishPaymentSeat/{reservationId}?clientId={clientId}&password={password}", HttpMethod.POST,
                    new HttpEntity<>(null, null), Void.class,
                    reservationId.getReservationId(), VALID_CLIENT_ID.getClientId(), PAYMENTS_MOCK_PASSWORD);

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            cleanUpSeatReservation(reservationId);
        }

        @Test
        void shouldReturnURL_whenRequestToPayAdvanceForHall() {
            // given
            final ReservationId reservationId = reserveHall();
            final String expectedURL = buildURL(reservationId, HALL_ADVANCE_COST, "advanceHall");

            // when
            final ResponseEntity<URL> response = restTemplate.exchange(
                    "/payAdvanceForHall/{reservationId}?clientId={clientId}", HttpMethod.POST,
                    new HttpEntity<>(null, null), URL.class,
                    reservationId.getReservationId(), VALID_CLIENT_ID.getClientId());

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(expectedURL, response.getBody().toString());
            cleanUpHallReservation(reservationId);
        }

        @Test
        void shouldNotPayAdvanceForHall_whenReservationNotFound() {
            // when
            final ResponseEntity<RestError> response = restTemplate.exchange(
                    "/payAdvanceForHall/{reservationId}?clientId={clientId}", HttpMethod.POST,
                    new HttpEntity<>(null, null), RestError.class,
                    INVALID_RESERVATION_ID.getReservationId(), VALID_CLIENT_ID.getClientId());

            // then
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Reservation with this ID not found", response.getBody().getMessage());
        }

        @Test
        void shouldNotPayAdvanceForHall_whenReservationAlreadyPaid() {
            // given
            final ReservationId reservationId = reserveHall();
            finishAdvanceHallPayment(reservationId);

            // when
            final ResponseEntity<RestError> response = restTemplate.exchange(
                    "/payAdvanceForHall/{reservationId}?clientId={clientId}", HttpMethod.POST,
                    new HttpEntity<>(null, null), RestError.class,
                    reservationId.getReservationId(), VALID_CLIENT_ID.getClientId());

            // then
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Advance payment for this reservation is already paid for", response.getBody().getMessage());
            cleanUpHallReservation(reservationId);
        }

        @Test
        void shouldFinishAdvanceHallPayment_whenValidPassword() {
            // given
            final ReservationId reservationId = reserveHall();

            // when
            final ResponseEntity<Void> response = restTemplate.exchange(
                    "/finishAdvancePaymentHall/{reservationId}?clientId={clientId}&password={password}", HttpMethod.POST,
                    new HttpEntity<>(null, null), Void.class,
                    reservationId.getReservationId(), VALID_CLIENT_ID.getClientId(), PAYMENTS_MOCK_PASSWORD);

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            cleanUpHallReservation(reservationId);
        }

        @Test
        void shouldReturnURL_whenRequestToPayForHall() {
            // given
            final ReservationId reservationId = reserveHall();
            final String expectedURL = buildURL(reservationId, HALL_COST, "hall");

            // when
            final ResponseEntity<URL> response = restTemplate.exchange(
                    "/payForHall/{reservationId}?clientId={clientId}", HttpMethod.POST,
                    new HttpEntity<>(null, null), URL.class,
                    reservationId.getReservationId(), VALID_CLIENT_ID.getClientId());

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(expectedURL, response.getBody().toString());
            cleanUpHallReservation(reservationId);
        }

        @Test
        void shouldPayLessForHall_whenAdvancePaid() {
            // given
            final ReservationId reservationId = reserveHall();
            finishAdvanceHallPayment(reservationId);
            final String expectedURL = buildURL(reservationId, HALL_COST-HALL_ADVANCE_COST, "hall");

            // when
            final ResponseEntity<URL> response = restTemplate.exchange(
                    "/payForHall/{reservationId}?clientId={clientId}", HttpMethod.POST,
                    new HttpEntity<>(null, null), URL.class,
                    reservationId.getReservationId(), VALID_CLIENT_ID.getClientId());

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(expectedURL, response.getBody().toString());
            cleanUpHallReservation(reservationId);
        }

        @Test
        void shouldNotPayForHall_whenReservationNotFound() {
            // when
            final ResponseEntity<RestError> response = restTemplate.exchange(
                    "/payForHall/{reservationId}?clientId={clientId}", HttpMethod.POST,
                    new HttpEntity<>(null, null), RestError.class,
                    INVALID_RESERVATION_ID.getReservationId(), VALID_CLIENT_ID.getClientId());

            // then
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Reservation with this ID not found", response.getBody().getMessage());
        }

        @Test
        void shouldNotPayForHall_whenReservationAlreadyPaid() {
            // given
            final ReservationId reservationId = reserveHall();
            finishHallPayment(reservationId);

            // when
            final ResponseEntity<RestError> response = restTemplate.exchange(
                    "/payForHall/{reservationId}?clientId={clientId}", HttpMethod.POST,
                    new HttpEntity<>(null, null), RestError.class,
                    reservationId.getReservationId(), VALID_CLIENT_ID.getClientId());

            // then
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Reservation with this ID is already paid for", response.getBody().getMessage());
            cleanUpHallReservation(reservationId);
        }

        @Test
        void shouldFinishHallPayment_whenValidPassword() {
            // given
            final ReservationId reservationId = reserveHall();

            // when
            final ResponseEntity<Void> response = restTemplate.exchange(
                    "/finishPaymentHall/{reservationId}?clientId={clientId}&password={password}", HttpMethod.POST,
                    new HttpEntity<>(null, null), Void.class,
                    reservationId.getReservationId(), VALID_CLIENT_ID.getClientId(), PAYMENTS_MOCK_PASSWORD);

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            cleanUpHallReservation(reservationId);
        }

        private String buildURL(ReservationId reservationId, double amount, String type) {
            return "http://localhost:9090/pay/" + reservationId.getReservationId() +
                    "?clientId="+ VALID_CLIENT_ID.getClientId() +
                    "&amount=" + amount +
                    "&type=" + type;
        }

        private void finishSeatPayment(ReservationId reservationId) {
            final ResponseEntity<Void> response = restTemplate.exchange(
                    "/finishPaymentSeat/{reservationId}?clientId={clientId}&password={password}", HttpMethod.POST,
                    new HttpEntity<>(null, null), Void.class,
                    reservationId.getReservationId(), VALID_CLIENT_ID.getClientId(), PAYMENTS_MOCK_PASSWORD);
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        private void finishAdvanceHallPayment(ReservationId reservationId) {
            final ResponseEntity<Void> response = restTemplate.exchange(
                    "/finishAdvancePaymentHall/{reservationId}?clientId={clientId}&password={password}", HttpMethod.POST,
                    new HttpEntity<>(null, null), Void.class,
                    reservationId.getReservationId(), VALID_CLIENT_ID.getClientId(), PAYMENTS_MOCK_PASSWORD);
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        private void finishHallPayment(ReservationId reservationId) {
            final ResponseEntity<Void> response = restTemplate.exchange(
                    "/finishPaymentHall/{reservationId}?clientId={clientId}&password={password}", HttpMethod.POST,
                    new HttpEntity<>(null, null), Void.class,
                    reservationId.getReservationId(), VALID_CLIENT_ID.getClientId(), PAYMENTS_MOCK_PASSWORD);
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Nested
    class ShowHallsTest {
        @Test
        void shouldReturnCinemaHalls_whenCinemaHallsExist() {
            // when
            final ResponseEntity<List<Hall>> response = restTemplate.exchange(
                    "/showAllCinemaHalls?clientId={clientId}", HttpMethod.GET,
                    new HttpEntity<>(null, null), new ParameterizedTypeReference<List<Hall>>() {},
                    VALID_CLIENT_ID.getClientId());

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
        }

        @Test
        void shouldReturnCinemaHalls_whenCinemaHallsAreAvailable() {
            // when
            final ResponseEntity<List<Hall>> response = restTemplate.exchange(
                    "/showAvailableCinemaHalls?clientId={clientId}&date={date}", HttpMethod.GET,
                    new HttpEntity<>(null, null), new ParameterizedTypeReference<List<Hall>>(){},
                    VALID_CLIENT_ID.getClientId(), FREE_DATE);

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
        }

        @Test
        void shouldReturnNotFound_whenCinemaHallsAreAllReserved() {
            // given
            final List<Hall> allHalls = getAllHalls();
            final List<ReservationId> reservationIds = new ArrayList<>();
            for (final Hall hall : allHalls) {
                reservationIds.add(reserveHall(hall.getHallId()));
            }

            // when
            final ResponseEntity<RestError> response = restTemplate.exchange(
                    "/showAvailableCinemaHalls?clientId={clientId}&date={date}", HttpMethod.GET,
                    new HttpEntity<>(null, null), RestError.class,
                    VALID_CLIENT_ID.getClientId(), FREE_DATE);

            // then
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Cannot find any cinema halls in this term", response.getBody().getMessage());
            for (final ReservationId reservationId : reservationIds) {
                cleanUpHallReservation(reservationId);
            }
        }

        @Test
        void shouldReturnHalls_whenOnlySomeHallsAreReserved() {
            // given
            final ReservationId reservationId = reserveHall();

            // when
            final ResponseEntity<List<Hall>> response = restTemplate.exchange(
                    "/showAvailableCinemaHalls?clientId={clientId}&date={date}", HttpMethod.GET,
                    new HttpEntity<>(null, null), new ParameterizedTypeReference<List<Hall>>() {},
                    VALID_CLIENT_ID.getClientId(), FREE_DATE);

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            cleanUpHallReservation(reservationId);
        }

        private List<Hall> getAllHalls() {
            final ResponseEntity<List<Hall>> response = restTemplate.exchange(
                    "/showAllCinemaHalls?clientId={clientId}", HttpMethod.GET,
                    new HttpEntity<>(null, null), new ParameterizedTypeReference<List<Hall>>() {},
                    VALID_CLIENT_ID.getClientId());
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            return response.getBody();
        }
    }

    @Nested
    class ShowSeatsTest {
        @Test
        void shouldReturnAllSeats_whenMovieExists() {
            // when
            final ResponseEntity<List<Seat>> response = restTemplate.exchange(
                    "/showAllSeats/{movieId}?clientId={clientId}", HttpMethod.GET,
                    new HttpEntity<>(null, null), new ParameterizedTypeReference<List<Seat>>(){},
                    VALID_MOVIE_ID, VALID_CLIENT_ID.getClientId());

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
        }

        @Test
        void shouldNotReturnAllSeats_whenMovieDoesntExist() {
            // when
            final ResponseEntity<RestError> response = restTemplate.exchange(
                    "/showAllSeats/{movieId}?clientId={clientId}", HttpMethod.GET,
                    new HttpEntity<>(null, null), RestError.class,
                    INVALID_MOVIE_ID, VALID_CLIENT_ID.getClientId());

            // then
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Movie with this ID not found", response.getBody().getMessage());
        }

        @Test
        void shouldReturnFreeSeats_whenSeatsAreAvailable() {
            // when
            final ResponseEntity<List<Seat>> response = restTemplate.exchange(
                    "/showFreeSeats/{movieId}?clientId={clientId}", HttpMethod.GET,
                    new HttpEntity<>(null, null), new ParameterizedTypeReference<List<Seat>>(){},
                    VALID_MOVIE_ID, VALID_CLIENT_ID.getClientId());

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
        }

        @Test
        void shouldReturnFreeSeats_whenSomeSeatsAreReserved() {
            // given
            final ReservationId reservationId = reserveSeat();

            // when
            final ResponseEntity<List<Seat>> response = restTemplate.exchange(
                    "/showFreeSeats/{movieId}?clientId={clientId}", HttpMethod.GET,
                    new HttpEntity<>(null, null), new ParameterizedTypeReference<List<Seat>>(){},
                    VALID_MOVIE_ID, VALID_CLIENT_ID.getClientId());

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            cleanUpSeatReservation(reservationId);
        }

        @Test
        void shouldNotReturnFreeSeats_whenMovieDoesntExist() {
            // when
            final ResponseEntity<RestError> response = restTemplate.exchange(
                    "/showFreeSeats/{movieId}?clientId={clientId}", HttpMethod.GET,
                    new HttpEntity<>(null, null), RestError.class,
                    INVALID_MOVIE_ID, VALID_CLIENT_ID.getClientId());

            // then
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Movie with this ID not found", response.getBody().getMessage());
        }

        @Test
        void shouldNotReturnFreeSeats_whenAllSeatsAreReserved() {
            // given
            final List<Seat> allSeats = getAllSeats();
            final List<ReservationId> reservationIds = new ArrayList<>();
            for (final Seat seat : allSeats) {
                reservationIds.add(reserveSeat(seat.getSeatId()));
            }

            // when
            final ResponseEntity<RestError> response = restTemplate.exchange(
                    "/showFreeSeats/{movieId}?clientId={clientId}", HttpMethod.GET,
                    new HttpEntity<>(null, null), RestError.class,
                    VALID_MOVIE_ID, VALID_CLIENT_ID.getClientId());

            // then
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("No seats available to reserve for this movie", response.getBody().getMessage());
            for (final ReservationId reservationId : reservationIds) {
                cleanUpSeatReservation(reservationId);
            }
        }

        private List<Seat> getAllSeats() {
            final ResponseEntity<List<Seat>> response = restTemplate.exchange(
                    "/showAllSeats/{movieId}?clientId={clientId}", HttpMethod.GET,
                    new HttpEntity<>(null, null), new ParameterizedTypeReference<List<Seat>>(){},
                    VALID_MOVIE_ID, VALID_CLIENT_ID.getClientId());
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            return response.getBody();
        }
    }

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

    private void cleanUpSeatReservation(final ReservationId reservationId) {
        final ResponseEntity<Void> response = restTemplate.exchange(
                "/reserveSeat/{reservationId}?clientId={clientId}", HttpMethod.DELETE,
                new HttpEntity<>(null, null), Void.class,
                reservationId.getReservationId(), VALID_CLIENT_ID.getClientId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private ReservationId reserveHall() {
        return reserveHall(VALID_HALL_ID);
    }

    private ReservationId reserveHall(final int hallId) {
        final ResponseEntity<ReservationId> reservationResponse = restTemplate.exchange(
                "/reserveHall/{hallId}/{date}?clientId={clientId}", HttpMethod.POST,
                new HttpEntity<>(null, null), ReservationId.class,
                hallId, FREE_DATE, VALID_CLIENT_ID.getClientId());
        assertEquals(HttpStatus.OK, reservationResponse.getStatusCode());
        assertNotNull(reservationResponse.getBody());
        return reservationResponse.getBody();
    }

    private void cleanUpHallReservation(final ReservationId reservationId) {
        final ResponseEntity<Void> response = restTemplate.exchange(
                "/reserveHall/{reservationId}?clientId={clientId}", HttpMethod.DELETE,
                new HttpEntity<>(null, null), Void.class,
                reservationId.getReservationId(), VALID_CLIENT_ID.getClientId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Nested
    class SpecialOffers{

        @Test
        void shouldNotAddSpecialOfferIfClientIsNotAdmin(){
            //given
            final String UNIQUE_CODE = new Random().nextLong() + "UniqueCode";
            final SpecialOfferAddRequest validSpecialOffer = new SpecialOfferAddRequest(UNIQUE_CODE, 25);
            //when
            final ResponseEntity<RestError> response = restTemplate.exchange(
                    "/addSpecialOffer?clientId={clientId}", HttpMethod.POST,
                    new HttpEntity<>(validSpecialOffer, null), RestError.class,
                    VALID_CLIENT_ID.getClientId());
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Only admin can do this", response.getBody().getMessage());

        }

        @Disabled("implement delete to test this properly")
        @Test
        void shouldAddSpecialOfferIfCodeIsUnique(){
            //given
            final String UNIQUE_CODE = new Random().nextLong() + "UniqueCode";
            final SpecialOfferAddRequest validSpecialOffer = new SpecialOfferAddRequest(UNIQUE_CODE, 25);
            //when
            final ResponseEntity<Void> response = restTemplate.exchange(
                    "/addSpecialOffer?clientId={clientId}", HttpMethod.POST,
                    new HttpEntity<>(validSpecialOffer, null), Void.class,
                    ADMIN_CLIENT_ID.getClientId());
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldNotAddSpecialOfferIfCodeIsNotUnique(){
            final ResponseEntity<RestError> response = restTemplate.exchange(
                    "/addSpecialOffer?clientId={clientId}", HttpMethod.POST,
                    new HttpEntity<>(existingCodeSpecialOffer, null), RestError.class,
                    ADMIN_CLIENT_ID.getClientId());
            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Special offer code already exists", response.getBody().getMessage());
        }

        @Test
        void shouldShowSpecialOffersIfExist(){
            final ResponseEntity<List<SpecialOffer>> response = restTemplate.exchange(
                    "/showSpecialOffers?clientId={clientId}", HttpMethod.GET,
                    new HttpEntity<>(null, null), new ParameterizedTypeReference<List<SpecialOffer>>(){},
                    ADMIN_CLIENT_ID.getClientId());
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
        }
    }


    @Nested
    class StatisticsTest {
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
                    ADMIN_CLIENT_ID.getClientId(),NO_MOVIES_FROM, NO_MOVIES_UP_TO);

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
                    ADMIN_CLIENT_ID.getClientId(),HALL_RESERVATION_DATE, PAST_DATE);

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
        void shouldReturnStatistics_whenThereAreReservationsThisDate(){
            //when
            final ResponseEntity<Statistics> response = restTemplate.exchange(
                    "/showStatistics?clientId={clientId}&from={from}&to={to}", HttpMethod.GET,
                    new HttpEntity<>(null, null), Statistics.class,
                    ADMIN_CLIENT_ID.getClientId(),PAST_DATE, HALL_RESERVATION_DATE);

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
        }

    }

}