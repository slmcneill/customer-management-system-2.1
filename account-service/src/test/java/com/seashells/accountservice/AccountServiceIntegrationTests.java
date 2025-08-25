package com.seashells.accountservice;

import com.seashells.accountservice.api.AccountRootController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.junit.jupiter.api.Disabled;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountServiceIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testRegisterMissingFields() {
        Map<String, String> payload = Map.of(
                "name", "",
                "user_name", "",
                "email", "",
                "password", ""
        );
        ResponseEntity<String> response = restTemplate.postForEntity("/account/register", payload, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Missing registration fields"));
    }

    @Test
    void testRegisterAndLoginSuccess() {
        String username = "testuser" + System.currentTimeMillis();
        Map<String, String> payload = Map.of(
                "name", "Test User",
                "user_name", username,
                "email", username + "@example.com",
                "password", "testpass"
        );
        ResponseEntity<String> regResponse = restTemplate.postForEntity("/account/register", payload, String.class);
        assertEquals(HttpStatus.OK, regResponse.getStatusCode());
        assertTrue(regResponse.getBody().contains("Registration successful"));

        Map<String, String> loginPayload = Map.of(
                    "name", username,
                "password", "testpass"
        );
        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/account/token", loginPayload, String.class);
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertTrue(loginResponse.getBody().contains("token"));
    }

    @Test
    @Disabled
    void testLoginInvalidCredentials() {
        Map<String, String> loginPayload = Map.of(
                "name", "nonexistentuser",
                "password", "wrongpass"
        );
        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/account/token", loginPayload, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, loginResponse.getStatusCode());
        assertTrue(loginResponse.getBody().contains("Invalid credentials"));
    }
}
