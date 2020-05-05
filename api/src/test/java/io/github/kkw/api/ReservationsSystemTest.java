package io.github.kkw.api;

import io.github.kkw.api.exceptions.RestError;
import io.github.kkw.api.model.ClientId;
import io.github.kkw.api.model.ReservationId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
    private static final ReservationId INVALID_RESERVATION_ID = new ReservationId(99_999_999);

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReserveSeat_whenSeatFreeToReserve() {
        // when
        final ResponseEntity<ReservationId> response = restTemplate.exchange(
                "/reserveSeat/{movieId}/{seatId}?clientId={clientId}", HttpMethod.POST,
                new HttpEntity<>(null, null), ReservationId.class,
                VALID_MOVIE_ID, VALID_SEAT_ID, VALID_CLIENT_ID.getId());

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        cleanUpReservation(response.getBody());
    }

    private void cleanUpReservation(final ReservationId reservationId) {
        final ResponseEntity<Void> response = restTemplate.exchange(
                "/reserveSeat/{reservationId}?clientId={clientId}", HttpMethod.DELETE,
                new HttpEntity<>(null, null), Void.class,
                reservationId.getId(), VALID_CLIENT_ID.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldNotReserveSeat_whenClientNotLoggedIn() {
        // given
        final ClientId clientId = registerClientAndLogout();

        // when
        final ResponseEntity<RestError> response = restTemplate.exchange(
                "/reserveSeat/{movieId}/{seatId}?clientId={clientId}", HttpMethod.POST,
                new HttpEntity<>(null, null), RestError.class,
                VALID_MOVIE_ID, VALID_SEAT_ID, clientId.getId());

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
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
                "/logout?email={email}&password={password}", HttpMethod.POST,
                new HttpEntity<>(null, null), Void.class,
                validEmail, password);
        assertEquals(HttpStatus.OK, logoutResponse.getStatusCode());

        return registerResponse.getBody();
    }

    @Test
    void shouldNotReserveSeat_whenMovieNotFound() {
        // when
        final ResponseEntity<RestError> response = restTemplate.exchange(
                "/reserveSeat/{movieId}/{seatId}?clientId={clientId}", HttpMethod.POST,
                new HttpEntity<>(null, null), RestError.class,
                INVALID_MOVIE_ID, VALID_SEAT_ID, VALID_CLIENT_ID.getId());

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Movie with this ID not found", response.getBody().getMessage());
    }

    @Test
    void shouldNotReserveSeat_whenSeatNotFound() {
        // when
        final ResponseEntity<RestError> response = restTemplate.exchange(
                "/reserveSeat/{movieId}/{seatId}?clientId={clientId}", HttpMethod.POST,
                new HttpEntity<>(null, null), RestError.class,
                VALID_MOVIE_ID, INVALID_SEAT_ID, VALID_CLIENT_ID.getId());

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
                "/reserveSeat/{movieId}/{seatId}?clientId={clientId}", HttpMethod.POST,
                new HttpEntity<>(null, null), RestError.class,
                VALID_MOVIE_ID, VALID_SEAT_ID, VALID_CLIENT_ID.getId());

        // then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Chosen seat for this movie is already reserved", response.getBody().getMessage());
        cleanUpReservation(reservationId);
    }

    @Test
    void shouldDeleteReservation_whenReservedByThisClient() {
        // given
        final ReservationId reservationId = reserveSeat();

        // when
        final ResponseEntity<Void> response = restTemplate.exchange(
                "/reserveSeat/{reservationId}?clientId={clientId}", HttpMethod.DELETE,
                new HttpEntity<>(null, null), Void.class,
                reservationId.getId(), VALID_CLIENT_ID.getId());

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldNotDeleteReservation_whenReservationNotFound() {
        // when
        final ResponseEntity<RestError> response = restTemplate.exchange(
                "/reserveSeat/{reservationId}?clientId={clientId}", HttpMethod.DELETE,
                new HttpEntity<>(null, null), RestError.class,
                INVALID_RESERVATION_ID.getId(), VALID_CLIENT_ID.getId());

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Reservation with this ID not found", response.getBody().getMessage());
    }

    private ReservationId reserveSeat() {
        final ResponseEntity<ReservationId> reservationResponse = restTemplate.exchange(
                "/reserveSeat/{movieId}/{seatId}?clientId={clientId}", HttpMethod.POST,
                new HttpEntity<>(null, null), ReservationId.class,
                VALID_MOVIE_ID, VALID_SEAT_ID, VALID_CLIENT_ID.getId());
        assertEquals(HttpStatus.OK, reservationResponse.getStatusCode());
        assertNotNull(reservationResponse.getBody());
        return reservationResponse.getBody();
    }
}