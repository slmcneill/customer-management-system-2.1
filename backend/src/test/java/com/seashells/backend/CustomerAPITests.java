package com.seashells.backend;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.seashells.backend.domain.Customer;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CustomerAPITests {
    @Autowired TestRestTemplate template;

    @Test
    public void testGetList() {
        Customer[] customers = template.getForObject("/api/customers", Customer[].class);
        assertNotNull(customers);
        assertTrue(customers.length > 0);
        assertNotNull(customers[0].getId());
    }

    @Test
    public void testGet() {
        Customer[] customers = template.getForObject("/api/customers", Customer[].class);
        assertNotNull(customers);
        Customer customer = template.getForObject("/api/customers/{id}", Customer.class, customers[0].getId());
        assertNotNull(customer);
        assertEquals(customers[0].getId(), customer.getId());
    }

    @Test
    public void testPost() {
        Customer customer = new Customer();
        customer.setName("Test User");
        customer.setUserName("testuser" + System.currentTimeMillis());
        customer.setEmailAddress("testuser" + System.currentTimeMillis() + "@test.com");
        customer.setPassword("testpass");

        URI location = template.postForLocation("/api/customers", customer, Customer.class);
        assertNotNull(location, "Location header should not be null");
        Customer savedCustomer = template.getForObject(location, Customer.class);
        assertNotNull(savedCustomer, "Saved customer should not be null");
        assertNotNull(savedCustomer.getId(), "Saved customer ID should not be null");
        assertEquals("Test User", savedCustomer.getName(), "Customer name should match");
        assertEquals(customer.getUserName(), savedCustomer.getUserName(), "Customer username should match");
        assertEquals(customer.getEmailAddress(), savedCustomer.getEmailAddress(), "Customer email should match");
        assertEquals("testpass", savedCustomer.getPassword(), "Customer password should match");
    }

    @Test
    public void testPut() {
        Customer[] customers = template.getForObject("/api/customers", Customer[].class);
        assertTrue(customers.length > 0);
        Customer customer = customers[0];
        String newValue = "NewValue" + Math.random();
        customer.setName(newValue);
        template.put("/api/customers/" + customer.getId(), customer);
        Customer updatedCustomer = template.getForObject("/api/customers/{id}", Customer.class, customer.getId());
        assertEquals(newValue, updatedCustomer.getName());
    }

    @Test
    public void testDelete() {
        Customer customer = new Customer();
        customer.setName("Delete User");
        customer.setUserName("deleteuser" + System.currentTimeMillis());
        customer.setEmailAddress("deleteuser" + System.currentTimeMillis() + "@test.com");
        customer.setPassword("deletepass");
        URI location = template.postForLocation("/api/customers", customer, Customer.class);
        assertNotNull(location);
        Customer savedCustomer = template.getForObject(location, Customer.class);
        assertNotNull(savedCustomer);
        template.delete("/api/customers/" + savedCustomer.getId());
        ResponseEntity<Customer> response = template.getForEntity("/api/customers/{id}", Customer.class, savedCustomer.getId());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}