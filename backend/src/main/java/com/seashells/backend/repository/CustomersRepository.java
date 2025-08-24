package com.seashells.backend.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.seashells.backend.domain.Customer;

@Repository
public interface CustomersRepository extends CrudRepository<Customer, Long> {
    Optional<Customer> findByEmailAddress(String emailAddress);

    Optional<Customer> findByUserName(String user_name);
}
