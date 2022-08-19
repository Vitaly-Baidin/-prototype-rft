package com.render.server.service;

import com.render.server.dto.request.CustomerRequest;
import org.springframework.http.ResponseEntity;

public interface CustomerService {
    ResponseEntity<?> createCustomer(CustomerRequest request);

    ResponseEntity<?> getAllCustomers();

    ResponseEntity<?> getCustomerById(Long id);

    ResponseEntity<?> updateCustomer(Long id, CustomerRequest request);

    ResponseEntity<?> deleteCustomer(Long id);
}
