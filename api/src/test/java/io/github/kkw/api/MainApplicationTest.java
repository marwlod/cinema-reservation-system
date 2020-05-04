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
                "/register?email={email}&password={email}", HttpMethod.POST,
                new HttpEntity<>(null, null), Void.class,
                validEmail, PASSWORD);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldReturnError_whenInvalidEmail() {
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
    void shouldReturnError_whenClientAlreadyExists() {
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
}