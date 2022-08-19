package com.render.server.repository;

import com.render.server.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByName(String name);

    Optional<Customer> findByName(String name);
}
