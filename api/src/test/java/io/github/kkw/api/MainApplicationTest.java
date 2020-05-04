package io.github.kkw.api;

import io.github.kkw.api.exceptions.RestError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MainApplicationTest {
    private static final String PASSWORD = "somepassword";
    private static final String INVALID_PASSWORD = "invalidpassword";
    private static final String INVALID_EMAIL = "domain.com";
    private static final Random RANDOM = new Random();
    private String validEmail;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void generateData() {
        validEmail = RANDOM.nextLong() + "@domain.com";
    }

    @Test
    void shouldLoadApplicationContext() {
        assertNotNull(applicationContext);
    }

    @Test
    void shouldRegisterClient_whenValidRequest() {
        // when
        final ResponseEntity<Void> response = restTemplate.exchange(
                "/register?email={email}&password={password}", HttpMethod.POST,
                new HttpEntity<>(null, null), Void.class,
                validEmail, PASSWORD);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldNotRegister_whenInvalidEmail() {
        // when
        final ResponseEntity<RestError> response = restTemplate.exchange(
                "/register?email={email}&password={password}", HttpMethod.POST,
                new HttpEntity<>(null, null), RestError.class,
                INVALID_EMAIL, PASSWORD);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("registerClient.email: must be a well-formed email address", response.getBody().getMessage());
    }

    @Test
    void shouldNotRegister_whenClientAlreadyExists() {
        // given
        restTemplate.exchange(
                "/register?email={email}&password={password}", HttpMethod.POST,
                new HttpEntity<>(null, null), RestError.class,
                validEmail, PASSWORD);

        // when
        final ResponseEntity<RestError> response = restTemplate.exchange(
                "/register?email={email}&password={password}", HttpMethod.POST,
                new HttpEntity<>(null, null), RestError.class,
                validEmail, PASSWORD);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Client with this email already exists", response.getBody().getMessage());
    }

    @Test
    void shouldLoginClient_whenValidRequest() {
        // given
        restTemplate.exchange(
                "/register?email={email}&password={password}", HttpMethod.POST,
                new HttpEntity<>(null, null), Void.class,
                validEmail, PASSWORD);

        // when
        final ResponseEntity<Void> response = restTemplate.exchange(
                "/login?email={email}&password={password}", HttpMethod.POST,
                new HttpEntity<>(null, null), Void.class,
                validEmail, PASSWORD);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldNotLogin_whenClientDoesntExist() {
        // when
        final ResponseEntity<RestError> response = restTemplate.exchange(
                "/login?email={email}&password={password}", HttpMethod.POST,
                new HttpEntity<>(null, null), RestError.class,
                validEmail, PASSWORD);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Client with this email not found", response.getBody().getMessage());
    }

    @Test
    void shouldNotLogin_whenWrongPassword() {
        // given
        restTemplate.exchange(
                "/register?email={email}&password={password}", HttpMethod.POST,
                new HttpEntity<>(null, null), Void.class,
                validEmail, PASSWORD);

        // when
        final ResponseEntity<RestError> response = restTemplate.exchange(
                "/login?email={email}&password={password}", HttpMethod.POST,
                new HttpEntity<>(null, null), RestError.class,
                validEmail, INVALID_PASSWORD);

        // then
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Wrong password", response.getBody().getMessage());
    }

    @Test
    void shouldLogoutClient_whenValidRequest() {
        // given
        restTemplate.exchange(
                "/register?email={email}&password={password}", HttpMethod.POST,
                new HttpEntity<>(null, null), Void.class,
                validEmail, PASSWORD);

        // when
        final ResponseEntity<Void> response = restTemplate.exchange(
                "/logout?email={email}&password={password}", HttpMethod.POST,
                new HttpEntity<>(null, null), Void.class,
                validEmail, PASSWORD);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldNotLogout_whenClientDoesntExist() {
        // when
        final ResponseEntity<RestError> response = restTemplate.exchange(
                "/logout?email={email}&password={password}", HttpMethod.POST,
                new HttpEntity<>(null, null), RestError.class,
                validEmail, PASSWORD);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Client with this email not found", response.getBody().getMessage());
    }
}