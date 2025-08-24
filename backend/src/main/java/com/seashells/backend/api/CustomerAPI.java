package com.seashells.backend.api;

import java.net.URI;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.seashells.backend.domain.Customer;
import com.seashells.backend.repository.CustomersRepository;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*") 
public class CustomerAPI {

    private final CustomersRepository repo;

    public CustomerAPI(CustomersRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public Iterable<Customer> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Customer> getCustomer(@PathVariable("id") long id) {
        return repo.findById(id);
    }

    @PostMapping
    public ResponseEntity<?> addCustomer(@RequestBody Customer newCustomer) {
        if (newCustomer.getName() == null
                || newCustomer.getEmailAddress() == null
                || newCustomer.getPassword() == null) {
            return ResponseEntity.badRequest().build();
        }

        // Generate a unique username
        String baseUsername = newCustomer.getName().toLowerCase().replaceAll("\\s+", "");
        String username = baseUsername;
        int counter = 1;

        while (repo.findByUserName(username).isPresent()) {
            username = baseUsername + counter;
            counter++;
        }

        newCustomer.setUserName(username);

        newCustomer = repo.save(newCustomer);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newCustomer.getId())
                .toUri();

        // Return the new user + assigned username
        return ResponseEntity.created(location).body(newCustomer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putCustomer(
            @RequestBody Customer customer,
            @PathVariable("id") long id) {
        if (customer.getId() == null
                || customer.getId() != id
                || customer.getName() == null
                || customer.getEmailAddress() == null) {
            return ResponseEntity.badRequest().build();
        }
        repo.save(customer);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable("id") long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
