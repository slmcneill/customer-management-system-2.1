package com.seashells.accountservice.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.seashells.accountservice.security.TokenService;

@RestController
@RequestMapping("/account")
@CrossOrigin(origins = "*") 
public class AccountRootController {

    private final RestTemplate restTemplate;
    private final TokenService tokenService;
    @Value("${VITE_CUSTOMER_URL:http://localhost:8080/api/customers}")
    private String CUSTOMER_API_URL;

    public AccountRootController(RestTemplate restTemplate, TokenService tokenService) {
        this.restTemplate = restTemplate;
        this.tokenService = tokenService;
    }

    @GetMapping
    public String root() {
        return "Account Service is up and running.";
    }

    public static class LoginRequest {
        public String name; 
        public String password;
    }

    public static class RegisterRequest {
        public String name;
        public String email;
        public String password;
    }

    public static class Customer {
        private Long id;
        private String name;
        private String password;

        @JsonProperty("email")
        private String emailAddress;

        @JsonProperty("user_name")
        private String userName;

        public Customer() {}

        public Customer(String name, String emailAddress, String password, String userName) {
            this.name = name;
            this.emailAddress = emailAddress;
            this.password = password;
            this.userName = userName;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return emailAddress; }
        public void setEmail(String emailAddress) { this.emailAddress = emailAddress; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getUser_name() { return userName; }
        public void setUser_name(String user_name) { this.userName = user_name; }
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        if (registerRequest.name == null || registerRequest.email == null || registerRequest.password == null) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error\":\"Missing registration fields\"}");
        }

        Customer newCustomer = new Customer(
                registerRequest.name,
                registerRequest.email,
                registerRequest.password,
                registerRequest.name // user_name
        );

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Customer> request = new HttpEntity<>(newCustomer, headers);

            ResponseEntity<?> response = restTemplate.postForEntity(CUSTOMER_API_URL, request, Object.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"message\":\"Registration successful\"}");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"error\":\"Registration failed\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error\":\"" + e.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }

    @PostMapping("/token")
    public ResponseEntity<String> token(@RequestBody LoginRequest loginRequest) {
        if (loginRequest.name == null || loginRequest.password == null) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error\":\"Missing name or password\"}");
        }

        try {
            Customer[] customers = restTemplate.getForObject(CUSTOMER_API_URL, Customer[].class);
            if (customers == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"error\":\"Customer service unavailable\"}");
            }

            for (Customer customer : customers) {
                // match by email, user_name, or name
                if ((loginRequest.name.equals(customer.getUser_name())) &&
                        loginRequest.password.equals(customer.getPassword())) {

                    org.springframework.security.core.Authentication auth =
                            new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                                    loginRequest.name, loginRequest.password, java.util.Collections.emptyList()
                            );

                    String jwt = tokenService.generateToken(auth);
                    String json = "{ \"token\": \"" + jwt + "\" }";

                    return ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(json);
                }
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error\":\"Invalid credentials\"}");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error\":\"" + e.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }
}
