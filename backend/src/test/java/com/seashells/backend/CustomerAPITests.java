package com.seashells.backend;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;

import com.seashells.backend.domain.Customer;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Disabled
public class CustomerAPITests {
    
    @Autowired TestRestTemplate template;
    
    @Test
    @Disabled
    public void testGetList() {
        Customer[] customers = 
            template.getForObject("/api/customers", Customer[].class);
        assertNotNull(customers);
        assertNotNull(customers[0]);
        assertNotNull(customers[0].getId());
        assertTrue(customers.length>0);
    }
    @Test
    @Disabled
    public void testGet() {
        Customer customer = 
        template.getForObject("/api/customers/{id}", Customer.class, 0);
        assertNotNull(customer);
        assertNotNull(customer.getId());
    }



    @Test
    @Disabled
    public void testPost() {
        Customer customer = new Customer();
        customer.setName("Test");
        customer.setEmailAddress("test@test.com");
    
        URI location = template.postForLocation("/api/customers", customer, Customer.class);
    
        assertNotNull(location, "Location header should not be null");
            Customer savedCustomer = template.getForObject(location, Customer.class);
            assertNotNull(savedCustomer, "Saved customer should not be null");
        assertNotNull(savedCustomer.getId(), "Saved customer ID should not be null");
        assertEquals("Test", savedCustomer.getName(), "Customer name should match");
        assertEquals("test@test.com", savedCustomer.getEmailAddress(), "Customer email should match");
    }


    @Test
    @Disabled
    public void testPut() {
        String path = "/api/customers/2";
        String newValue = "NewValue" + Math.random();
        Customer customer = template.getForObject(path, Customer.class );
        customer.setName(newValue);
        template.put(path, customer);
        customer = template.getForObject(path, Customer.class );
        assertEquals(newValue, customer.getName());
    }

}